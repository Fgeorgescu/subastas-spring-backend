package com.subastas.virtual.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "items")
public class Item {

    public static final String STATUS_PROCESSING = "PROCESSING"; // Pendiente de aprobación
    public static final String STATUS_WAITING_AUCTION = "WAITING_AUCTION"; // Esperando que la vinculen a una subasta
    public static final String STATUS_PENDING = "PENDING"; // Esperando ser subastada, ya está vinculado
    public static final String STATUS_ACTIVE = "ACTIVE"; // En subasta
    public static final String STATUS_FINISHED = "FINISHED"; // Ya subastada


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String title;

    @Column
    private String description;

    @Column
    private int auction;

    @Column
    private String status;

    @Column
    private int owner;

    @ElementCollection
    @CollectionTable(name="imageUrls")
    @JsonProperty("image_urls")
    private List<String> imageUrls;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "itemId")
    private List<BidLog> biddings;

    @Column
    private float basePrice;

    @Column
    private float currentPrice;

    public Item(RegisterItemRequest request, User user) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.imageUrls = request.getImageUrls();
        this.status = STATUS_PROCESSING;
        this.owner = user.getId();
    }

}
