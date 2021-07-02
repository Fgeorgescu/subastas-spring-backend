package com.subastas.virtual.service;

import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.bid.http.BidLogWrapper;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.repository.ItemRepository;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private static ItemRepository itemRepositoryStatic;
  private final ItemRepository itemRepository;
    private final UserService userService;

  public ItemService(ItemRepository itemRepository, UserService userService) {
    this.itemRepositoryStatic = itemRepository;
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

  /**
   * Esta aberración es posible gracias a que nuestro servicio es un Bean.
   *
   * Spring instancia todos los Beans como singletons cuando levanta la app. Consecuentemente, si bien es un atributo
   * estático, se instancia el Bean y queda disponible, por eso no tira NPE.
   *
   * Esto lo necesitamos para acualizar el Item desde dentro de un auction. Si llegamos a refactorizar cambiamos esto.
   * @param item
   */
  public static void saveItemStatic(Item item) {
    itemRepositoryStatic.save(item);
  }
}
