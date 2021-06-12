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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String title;

    @Column
    private String category;

    @OneToMany(mappedBy = "auction")
    private List<RegisteredItem> items;

    public Auction(CreateAuctionRequest request) {
        this.title = request.getTitle();
        this.category = request.getCategory();
    }

}
