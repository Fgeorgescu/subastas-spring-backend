package com.subastas.virtual.repository;

import com.subastas.virtual.dto.bid.BidLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<BidLog, Integer> {
}
