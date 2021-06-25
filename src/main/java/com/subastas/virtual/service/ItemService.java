package com.subastas.virtual.service;

import com.subastas.virtual.dto.item.Item;
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

    public Item registerItem(RegisterItemRequest request, User user) {
        return itemRepository.save(new Item(request, user));
    }

    public Item getItem(int itemId) {

        return itemRepository.findById(itemId).orElseThrow(
            () -> new NotFoundException("item", itemId)
        );
    }

  public Item saveItem(Item item) {
        return itemRepository.save(item);
  }
}
