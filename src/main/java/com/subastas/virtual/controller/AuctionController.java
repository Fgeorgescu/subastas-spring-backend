package com.subastas.virtual.controller;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.service.AuctionService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auctions")
public class AuctionController {

    AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewAuction(@RequestBody CreateAuctionRequest request) {
        Auction auction = auctionService.createAuction(request);

        return ResponseEntity
                .created(URI.create(
                        String.format("/auctions/%d", auction.getId())))
                .body(auction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable("id") int auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);

        return ResponseEntity.ok(auction);
    }
}
