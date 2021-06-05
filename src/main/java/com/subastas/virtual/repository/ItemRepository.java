package com.subastas.virtual.repository;

import com.subastas.virtual.dto.item.RegisteredItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<RegisteredItem, Integer> {
}
