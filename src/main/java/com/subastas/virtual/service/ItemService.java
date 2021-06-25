package com.subastas.virtual.service;

import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public RegisteredItem registerItem(RegisterItemRequest request, User user) {
        return itemRepository.save(new RegisteredItem(request, user));
    }

    public RegisteredItem getItem(int itemId) {
        RegisteredItem item = itemRepository.findById(itemId).orElseThrow(
            () -> new NotFoundException("item", itemId)
        );

        return  item;
    }
}
