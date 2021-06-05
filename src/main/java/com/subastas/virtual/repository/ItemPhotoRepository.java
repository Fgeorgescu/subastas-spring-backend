package com.subastas.virtual.repository;

import com.subastas.virtual.dto.item.ItemPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPhotoRepository extends JpaRepository<ItemPhoto, Integer> {
}
