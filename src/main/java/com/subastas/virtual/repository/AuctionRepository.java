package com.subastas.virtual.repository;

import com.subastas.virtual.dto.auction.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
