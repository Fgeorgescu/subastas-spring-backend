package com.subastas.virtual.service;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.repository.AuctionRepository;
import com.subastas.virtual.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    AuctionRepository auctionRepository;
    ItemRepository itemRepository;
    UserService userService;
    Logger log = LoggerFactory.getLogger(this.getClass());


    public AuctionService(AuctionRepository auctionRepository,
                          ItemRepository itemRepository,
                          UserService userService) {
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public Auction createAuction(CreateAuctionRequest request) {
        Auction auction = new Auction(request);
        auction = auctionRepository.save(auction);

        // Obtenemos los items que vamos a usar
        List<Item> items = itemRepository.findAllById(request.getItemIds());

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
        auction = auctionRepository.save(auction);
        itemRepository.saveAll(items);
        return auction;
    }

    public Auction getAuctionById(int auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("auction", auctionId));
    }

    public List<Item> getAuctionItemsById(int auctionId) {
        return itemRepository.findAllByAuction(auctionId);
    }

    public List<Auction> getAuctionByStatus(String status) {
        return auctionRepository.findAllByStatus(status);
    }

    public List<Auction> getAuctions() {
        return auctionRepository.findAll();
    }

  public void addParticipant(int auctionId, User userCached) {

        Auction auction = getAuctionById(auctionId);
        userService.addAuction(auction, userCached.getId());
  }

    public List<User> getUsers(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NotFoundException("auction", auctionId));

        return auction.getUsers();
    }
}
