package com.subastas.virtual.dto.item;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_photo")
@Data
public class ItemPhoto {

    @Id
    @GeneratedValue
    int id;

    @Lob
    byte[] content;

    String name;
}
