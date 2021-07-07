package com.subastas.virtual.dto.auction;

import com.fasterxml.jackson.annotation.*;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.constantes.Category;
import com.subastas.virtual.dto.constantes.Currency;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.service.AuctionService;
import com.subastas.virtual.service.ItemService;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auction {

    public static final Logger log = LoggerFactory.getLogger(Auction.class);

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_FINISHED = "FINISHED";

    /*
    private static final String CATEGORY_COMUN = "COMUN";
    private static final String CATEGORY_PLATA = "PLATA";
    private static final String CATEGORY_ORO = "ORO";
    private static final String CATEGORY_DIAMANTE = "DIAMANTE";
    */

    private static final Long DURATION_IN_MINUTES = 1L; // 10 minutes
    private static final Long DURATION_IN_MILI = DURATION_IN_MINUTES*60*1000; // 10 minutes

    @Transient
    @JsonIgnore
    @ToString.Exclude
    private AuctionTask activeTask = new AuctionTask();


    @Transient
    @JsonIgnore
    @ToString.Exclude
    private Timer timer = new Timer();

    @Transient
    @JsonIgnore
    @ToString.Exclude
    private TimerTask startTask;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    @Enumerated
    private Category category;

    @Column
    private String status;

    @Column
    private LocalDateTime activeUntil;

    @OneToMany(mappedBy = "auction")
    private List<Item> items = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(targetEntity = User.class, mappedBy = "auctions", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Column()
    private Integer activeItem = -1;

    @Column
    private LocalDateTime startTime;

    @Column(columnDefinition = "int default 0") // por defecto pesos
    @Enumerated
    private Currency currency;

    public Auction(CreateAuctionRequest request) {
        this.title = request.getTitle();
        this.status = STATUS_PENDING;
        this.category = Category.COMUN;
        this.startTime = request.getStartingTime();
        this.currency = request.getCurrency();
    }

    public void startAuction() {
        log.info("startAuction - vamos a comenzar");
        // Esto activa el primer item
        activateNextItem();
        // Activamos la subasta e indicamos el momento del cierre
        this.status = STATUS_ACTIVE;
        this.startTime = this.startTime == null ? LocalDateTime.now() : this.startTime;
    }

    /**
     * Todas estas acciones modifica el auction y despues persistimos. Pero el item status
     * no podemos :( O SI????
     *
     * Acá está el problema, como esto se llama desde la misma subasta, no tengo como actualziar.
     *
     * moverlo al servicio implica que refactorice como se manejan los crons :(
     */
    public void activateNextItem() {

        // Finalizamos el item actual si existe
        if (activeItem != null && activeItem != -1) {
            log.info("Finalizando el item: {}", activeItem);
            Item item = this.getItems().stream()
                .filter(i -> i.getId() == activeItem).findFirst()
                .orElseThrow(() -> new NotFoundException("Problem, there should be an item here"));

            Optional<BidLog> winningBid = item.getBiddings().stream()
                .filter(b -> b.getBid() == item.getCurrentPrice()).findFirst();

            // Si tenemos una puja ganadora, ponemos ese usuario como el ganador
            winningBid.ifPresent(bidLog -> item.setWinnerId(bidLog.getBidder()));

            item.setStatus(STATUS_FINISHED); // TODO: Esto se puede sacar a un metodo del item para que el Auction no sepa de estados de Items.
            ItemService.saveItemStatic(item); // Feo feo
        }

        // Buscamos el siguiente (o primer) item
        Optional<Item> nextItemOptional = this.getItems().stream()
            .filter(i -> i.getStatus().equals(Item.STATUS_PENDING) ||
                i.getStatus().equals(Item.STATUS_PROCESSING)).findFirst(); // Tenemos en cuenta Processing para tener items
                // TODO: Cuando tengamos algo que apruebe los items (o los carguemos como tal) sacar el processing

        // Si no tenemos más items, cerramos la subasta
        if (nextItemOptional.isEmpty()) {
            log.info("No encontramos más items para la subasta {}, cerramos.", id);
            this.closeAuction();
        } else { // Si tenemos otro item activamos la subasta para el mismo.
            Item nextItem = nextItemOptional.get();
            this.activeItem = nextItem.getId();
            log.info("Próximo item: {}. Ya se encuentra activo.", activeItem);

            nextItem.setStatus(Item.STATUS_ACTIVE);
            startTimer(); // Esto tiene que estar acá para que se actualize this.activeUntil
            nextItem.setActiveUntil(this.activeUntil);
            ItemService.saveItemStatic(nextItem); // Feo feo
            AuctionService.saveAuctionStatic(this); // "Si no, no se actualiza el "active until"
        }

    }

    private void closeAuction() {
        log.info("Finishing auction: {}", id);
        activeItem = null;
        status = STATUS_FINISHED;

        AuctionService.saveAuctionStatic(this);
    }

    // Se llama del service, no necesita del Static save
    public void resetTimer() {
        log.info("Resetting timer for auction {} and item {}.", id, activeItem);
        activeTask.cancel();
        activeTask = new AuctionTask();
        timer.schedule(activeTask, DURATION_IN_MILI);
        this.activeUntil = calculateNextActiveUntil();
    }

    // Quien lo llama tiene la responsabilidad de gaurdar
    public void startTimer() {
        log.info("Iniciamos el timer para la subasta: {}", id);
        activeTask = new AuctionTask();
        timer.schedule(activeTask, DURATION_IN_MILI);
        this.activeUntil = calculateNextActiveUntil();
    }

    /**
     * When executed, it activates the next item for an auction,
     * or finishes said auction if no more items are available.
     */
    class AuctionTask extends TimerTask {
        @Override
        public void run() {
            log.info("AuctionTask triggered, moving forward with the auction.");
            activateNextItem();
        }
    };

    private LocalDateTime calculateNextActiveUntil() {
        return LocalDateTime.now().plusMinutes(DURATION_IN_MINUTES);
    }
}
