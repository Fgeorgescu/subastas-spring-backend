package com.subastas.virtual.controller;

import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.service.ItemService;
import com.subastas.virtual.utils.FileUploadUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @SneakyThrows
    @PostMapping("/items")
    public ResponseEntity<?> registerItem(@RequestBody RegisterItemRequest request) {
        log.info("Creating new Item record with information: {}", request);
        return ResponseEntity
                .created(URI.create("/test"))
                .body(itemService.registerItem(request));
    }

    @PostMapping("/items/{id}/photos")
    public ResponseEntity<?> uploadPhoto(@PathVariable("id") int itemId, @RequestParam("image") MultipartFile file) {
        RegisteredItem updatedItem = itemService.attachPicture(itemId, file);

        return ResponseEntity.created(URI.create("/test")).body(updatedItem);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<RegisteredItem> getItemById(@PathVariable("id") int itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }

    @GetMapping("/items/{id}/photos/{photoId}")
    public ResponseEntity<?> getItemPhotoById(@PathVariable("id") int itemId, @PathVariable("photoId") int photoId) {
        return ResponseEntity.ok(itemService.getItemPhoto(itemId, photoId));
    }
}
