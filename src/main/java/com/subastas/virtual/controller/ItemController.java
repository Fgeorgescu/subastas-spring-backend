package com.subastas.virtual.controller;

import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.bid.http.BidLogWrapper;
import com.subastas.virtual.dto.bid.http.BidRequest;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.service.BiddingService;
import com.subastas.virtual.service.ItemService;
import com.subastas.virtual.service.SessionService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/items")
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final BiddingService biddingService;

    public ItemController(ItemService itemService, BiddingService biddingService) {
        this.itemService = itemService;
        this.biddingService = biddingService;
    }

    @SneakyThrows
    @PostMapping("")
    public ResponseEntity<?> registerItem(@RequestBody RegisterItemRequest request, HttpSession session) {
        User user = SessionService.getUser(session);

        log.info("Creating new Item record with information: {}", request);
        return ResponseEntity
                .created(URI.create("/test"))
                .body(itemService.registerItem(request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") int itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }

    // Biddings
    @PostMapping("/{id}/bids")
    public ResponseEntity<?> processBid(@PathVariable("id") int itemId, @RequestBody BidRequest bid, HttpSession session) {
        User user = SessionService.getUser(session);


        return ResponseEntity.ok(biddingService.processBid(bid, itemId, user.getId()));
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<?> getBidsForItem(@PathVariable("id") int itemId) {
        List<BidLogWrapper> log = itemService.getItemBids(itemId);

        return ResponseEntity.ok(log);
    }
}
