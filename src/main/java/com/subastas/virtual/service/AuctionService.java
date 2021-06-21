package com.subastas.virtual.service;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.repository.AuctionRepository;
import com.subastas.virtual.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    AuctionRepository auctionRepository;
    ItemRepository itemRepository;

    public AuctionService(AuctionRepository auctionRepository, ItemRepository itemRepository) {
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
    }

    public Auction createAuction(CreateAuctionRequest request) {
        Auction auction = new Auction(request);
        auction = auctionRepository.save(auction);

        // Obtenemos los items que vamos a usar
        List<RegisteredItem> items = itemRepository.findAllById(request.getItemIds());

        // Validamos que todos los items existan.
        if (items.size() != request.getItemIds().size()) {
            auctionRepository.delete(auction); //Rollback
            throw new RequestConflictException("Not every item requested was found");
        }

        // Validamos que los items no estén asignados a otra subasta
        // Me quedo con los que id auction id != 0 -> Están en una subasta
        List<RegisteredItem> assignedItems = items.stream().filter(i -> i.getAuction() != 0 ).collect(Collectors.toList());
        if (!assignedItems.isEmpty()) { // La lista debería ser vacia
            auctionRepository.delete(auction); // Rollback
            throw new RequestConflictException(String.format("Items %s already assigned to an auction", assignedItems));
        }

        // Actualizamos el id de la subasta en los items
        for(RegisteredItem i : items) {
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

    public Auction getAuctionItemsById(int auctionId) {
        return null;
    }

    public List<Auction> getAuctionByStatus(String status) {
        return auctionRepository.findAllByStatus(status);
    }

    public List<Auction> getAuctions() {
        return auctionRepository.findAll();
    }
}
