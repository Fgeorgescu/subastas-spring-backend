package com.subastas.virtual.repository;

import com.subastas.virtual.dto.item.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
  List<Item> findAllByAuction(int auctionId);

  long countAllByWinnerId(int winnerId);
}
