package com.subastas.virtual.dto.auction;

import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.item.RegisteredItem;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Auction {

    public static final String STATUS_PENDING = "pending";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String category;

    @Column
    private String status;

    @OneToMany(mappedBy = "auction")
    private List<RegisteredItem> items;

    public Auction(CreateAuctionRequest request) {
        this.title = request.getTitle();
        this.category = request.getCategory();
        this.status = STATUS_PENDING;
    }

}
