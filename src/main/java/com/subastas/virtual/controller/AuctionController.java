package com.subastas.virtual.controller;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.service.AuctionService;
import com.subastas.virtual.service.SessionService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/auctions")
public class AuctionController {

    AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewAuction(@RequestBody CreateAuctionRequest request, HttpSession session) {
        UserInformation user = SessionService.getUser(session);

        Auction auction = auctionService.createAuction(request);

        return ResponseEntity
                .created(URI.create(
                        String.format("/auctions/%d", auction.getId())))
                .body(auction);
    }

    @GetMapping("")
    public ResponseEntity<List<Auction>> getAuction(@RequestParam(name = "status", defaultValue = "") String status) {
        if ("".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(auctionService.getAuctions());
        }

        auctionService.getAuctionByStatus(status);

        return ResponseEntity.ok(auctionService.getAuctionByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable("id") int auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);

        return ResponseEntity.ok(auction);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<Auction> getAuctionItems(@PathVariable("id") int auctionId) {
        Auction auction = auctionService.getAuctionItemsById(auctionId);

        return ResponseEntity.ok(auction);
    }


}
