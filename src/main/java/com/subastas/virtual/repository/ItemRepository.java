package com.subastas.virtual.repository;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.item.RegisteredItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<RegisteredItem, Integer> {
  List<RegisteredItem> findAllByAuction(int auctionId);
}
