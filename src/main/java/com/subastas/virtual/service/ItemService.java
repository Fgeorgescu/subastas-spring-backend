package com.subastas.virtual.service;

import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.bid.http.BidLogWrapper;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

  public ItemService(ItemRepository itemRepository, UserService userService) {
    this.itemRepository = itemRepository;
    this.userService = userService;
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

  public List<BidLogWrapper> getItemBids(int itemId) {
      List<BidLog> logs = getItem(itemId).getBiddings();
      List<BidLogWrapper> logsWrapped = logs.stream().map(b -> {
        User user = userService.getUser(b.getBidder());
        Item item = getItem(itemId);
        return new BidLogWrapper(b, user.getUsername(), item.getTitle());
      }).collect(Collectors.toList());

      return logsWrapped;
  }
}
