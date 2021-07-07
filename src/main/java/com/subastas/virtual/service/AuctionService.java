package com.subastas.virtual.service;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.constantes.Currency;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.repository.AuctionRepository;
import com.subastas.virtual.repository.ItemRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    private AuctionRepository auctionRepository;
    private static AuctionRepository auctionRepositoryStatic;
    private ItemRepository itemRepository;
    private UserService userService;

    /**
     * Handles auctions auto-start.
     */
    Timer auctionStarterTimer = new Timer();
    Logger log = LoggerFactory.getLogger(this.getClass());


    public AuctionService(AuctionRepository auctionRepository,
                          ItemRepository itemRepository,
                          UserService userService) {
        this.auctionRepository = auctionRepository;
        this.auctionRepositoryStatic = auctionRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public Auction createAuction(CreateAuctionRequest request) {
        Auction auction = new Auction(request);
        auction = auctionRepository.save(auction); // TODO: Validar si esto podemos moverlo al final del método, elimina la necesidad de eliminar a mano la subasta
        final Currency currency = auction.getCurrency();

        if (auction.getItems().isEmpty()) {
            throw new RequestConflictException("There should be at least one item in an auction");
        }

        // Obtenemos los items que vamos a usar
        List<Item> items = itemRepository.findAllById(request.getItemIds());

        // Validamos que todos sean de la moneda correcta. Si no tiramos un error
        if (items.stream().anyMatch(i -> i.getCurrency().compareTo(currency) != 0)) {
            auctionRepository.delete(auction); //Rollback
            throw new RequestConflictException("Items should be in " + auction.getCurrency());
        }

        // Validamos que todos los items existan.
        if (items.size() != request.getItemIds().size()) {
            auctionRepository.delete(auction); //Rollback
            throw new RequestConflictException("Not every item requested was found");
        }

        // Validamos que los items no estén asignados a otra subasta
        // Me quedo con los que id auction id != 0 -> Están en una subasta
        List<Item> assignedItems = items.stream().filter(i -> i.getAuction() != 0 ).collect(Collectors.toList());
        if (!assignedItems.isEmpty()) { // La lista debería ser vacia
            auctionRepository.delete(auction); // Rollback
            throw new RequestConflictException(String.format("Items %s already assigned to an auction", assignedItems));
        }

        // Actualizamos el id de la subasta en los items
        for(Item i : items) {
            i.setAuction(auction.getId());
        }
        // Asignamos los items a la subasta
        auction.setItems(items);

        // Persistimos la subasta y devolvemos la nueva entidad
        itemRepository.saveAll(items);

        if (auction.getStartTime() != null) {
            if (auction.getStartTime().compareTo(LocalDateTime.now()) < 0) {
                throw new RequestConflictException("Start time can not be a past time");
            }

            log.info("Setting auction auto start");
            auction.setStartTask(new CronAuctionTask(auction));

            auctionStarterTimer.schedule(auction.getStartTask(), Timestamp.valueOf(auction.getStartTime()));
        }

        auction = auctionRepository.save(auction);
        return auction;
    }

    public Auction getAuctionById(int auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("auction", auctionId));
    }

    public List<Item> getAuctionItemsById(int auctionId) {
        return itemRepository.findAllByAuction(auctionId);
    }

    public List<Auction> getAuctionByStatus(String[] status) {
        return auctionRepository.findAllByStatusIn(status);
    }

    public List<Auction> getAuctions() {
        return auctionRepository.findAll();
    }

  public void addParticipant(int auctionId, User userCached) {
        User actualUser = userService.getUser(userCached.getId());
        Auction auction = getAuctionById(auctionId);

      if (auction.getStatus().equalsIgnoreCase(Auction.STATUS_FINISHED)) {
          throw new RequestConflictException("Auction already finished, you can not register.");
      }

        if (actualUser.getAuctions().contains(auction)) {
            throw new RequestConflictException("User already registered for this auction.");
        }

        // Si el usuario NO tiene la misma categoría o superior, no se puede registrat
        if (!userCached.getCategory().isCategoryGreaterOrEqualsThan(auction.getCategory())) {
            throw new RequestConflictException("You should be category " + auction.getCategory() + " or higher");
        }

        userService.addAuction(auction, actualUser.getId());
  }

    public List<User> getUsers(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NotFoundException("auction", auctionId));

        return auction.getUsers();
    }

    public Auction startAuction(int auctionId) {
        Auction auction = getAuctionById(auctionId);

        if (auction.getStartTask() != null) {
            auction.getStartTask().cancel();
        }

        auction.startAuction();
        return auctionRepository.save(auction);
    }

    /**
     * Reiniciamos la subasta pero actualizamos el active item
     * @param auctionId
     * @return
     */
    public Auction resetAuctionTimer(int auctionId) {
        log.info("Resetting timer for auction: {}", auctionId);

        Auction auction = getAuctionById(auctionId);
        auction.resetTimer();
        auctionRepository.save(auction);
        return auction;
    }

    class CronAuctionTask extends TimerTask {
        private final Auction auction;

        public CronAuctionTask(Auction auction) {
            this.auction = auction;
        }

        @Override
        public void run() {
            log.info("Automatically starting auction: {}", auction.getId());
            auction.startAuction();
        }
    }

    /**
     * Esta aberración es posible gracias a que nuestro servicio es un Bean.
     *
     * Spring instancia todos los Beans como singletons cuando levanta la app. Consecuentemente, si bien es un atributo
     * estático, se instancia el Bean y queda disponible, por eso no tira NPE.
     *
     * Esto lo necesitamos para acualizar el Item desde dentro de un auction. Si llegamos a refactorizar cambiamos esto.
     * @param auction
     */
    public static void saveAuctionStatic(Auction auction) {
        auctionRepositoryStatic.save(auction);
    }
}
