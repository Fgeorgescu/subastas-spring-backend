package com.subastas.virtual.service;

import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.bid.http.BidRequest;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.repository.BidRepository;
import org.springframework.stereotype.Service;

@Service
public class BiddingService {

  BidRepository bidRepository;
  ItemService itemService;
  UserService userService;
  AuctionService auctionService;

  public BiddingService(BidRepository bidRepository, ItemService itemService, UserService userService, AuctionService auctionService) {
    this.bidRepository = bidRepository;
    this.itemService = itemService;
    this.userService = userService;
    this.auctionService = auctionService;
  }

  public Item processBid(BidRequest bid, int itemId, int userId) {
    User user = userService.getUser(userId);
    Item item = itemService.getItem(itemId);

    float bidAmount = bid.getBid();
    float current = item.getCurrentPrice();
    float delta = bid.getBid() - current;

    if (delta <= 0) { // Entonces la puja es menor al precio actual
      throw new RequestConflictException("Your bid is not high enough. It should be more than $" + current);
    }

    item.setCurrentPrice(bidAmount);
    itemService.saveItem(item);
    BidLog log = new BidLog(bidAmount, delta, user.getId(), item.getId());
    bidRepository.save(log);
    auctionService.resetAuctionTimer(item.getAuction());
    return item;
  }
}
