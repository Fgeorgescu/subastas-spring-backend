package com.subastas.virtual.repository;

import com.subastas.virtual.dto.auction.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
  List<Auction> findAllByStatusIn(String[] status);
}
