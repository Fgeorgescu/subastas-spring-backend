package com.subastas.virtual.service;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.bid.http.BidRequest;
import com.subastas.virtual.dto.constantes.Category;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.repository.BidRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BiddingService {

  BidRepository bidRepository;
  ItemService itemService;
  UserService userService;
  AuctionService auctionService;

  /**
   * Cota inferior para una puja. Representa el % que debe aumantar una puja el precio actual del articulo
   */
  private static final float MIN_INCREASE_PERCENT = 0.01f;

  /**
   * Cota superior para una puja. Representa el máximo porcentaje que se puede aumentar el valor de un artículo
   */
  private static final float MAX_INCREASE_PERCENT = 0.20f;

  /**
   * Tipos de subastas que no tienen limite máximo.
   * Las constantes deben ser UPPERCASE para asegurarnos que no importe el casing del input, ya que
   * se normaliza con .toUppercase()
   */
  private static final List<Category> LIMITLESS_AUCTION_TYPES = Arrays.asList(Category.ORO, Category.PLATINO);

  public BiddingService(BidRepository bidRepository, ItemService itemService, UserService userService, AuctionService auctionService) {
    this.bidRepository = bidRepository;
    this.itemService = itemService;
    this.userService = userService;
    this.auctionService = auctionService;
  }

  public Item processBid(BidRequest bid, int itemId, int userId) {
    User user = userService.getUser(userId);
    Item item = itemService.getItem(itemId);
    Auction auction = auctionService.getAuctionById(item.getAuction());

    // TODO: Se podría atomizar la lógica que calcila el minimo y máximo dependiendo de la subasta,
    //  el código duplicado puede traer errores. Pero ahora al menos pomemos manejar las variables para afectar el
    //  comportamiento
    float bidAmount = bid.getBid();
    float current = item.getCurrentPrice();
    float delta = bid.getBid() - current;

    // Validamos que las pujas estén dentro de las reglas de negocio.
    if (delta <= 0) { // La puja es menor al precio actual.
      throw new RequestConflictException("Your bid is not high enough. It should be more than $" +
          current * (1+ MIN_INCREASE_PERCENT));

    } else if (delta < current * MIN_INCREASE_PERCENT) { // Si la puja es menor al 1% del item, no debería procesarla.
      throw new RequestConflictException("The bid does not increase the price enough. It should be more than $" +
          current * (1+ MIN_INCREASE_PERCENT));

    } else if (!LIMITLESS_AUCTION_TYPES.contains(auction.getCategory()) && // Pujas que estén en la lista (true) deberían no entrar, por eso el !.
        delta > current * MAX_INCREASE_PERCENT) { // En Pujas que no son Si la puja es mayor al 20% del item, no debería procesarla.
      throw new RequestConflictException("Your bid is too high. It should be less than $" +
          current * (1+ MAX_INCREASE_PERCENT));

    }

    item.setCurrentPrice(bidAmount);
    BidLog log = new BidLog(bidAmount, delta, user.getId(), item.getId());
    item.setActiveUntil(auction.getActiveUntil());

    bidRepository.save(log);
    auctionService.resetAuctionTimer(item.getAuction());
    itemService.saveItem(item); // FEO

    return item;
  }

  public long getAmountOfBidsByUserId(int userId) {
    return bidRepository.countBidLogsByBidder(userId);
  }
}
