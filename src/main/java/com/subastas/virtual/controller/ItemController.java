package com.subastas.virtual.controller;

import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.service.ItemService;
import com.subastas.virtual.service.SessionService;
import java.net.URI;
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

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
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
    public ResponseEntity<RegisteredItem> getItemById(@PathVariable("id") int itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }
}
