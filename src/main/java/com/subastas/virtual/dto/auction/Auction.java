package com.subastas.virtual.dto.auction;

import com.fasterxml.jackson.annotation.*;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.constantes.Category;
import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.user.User;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auction {

    public static final String STATUS_PENDING = "pending";

    private static final String CATEGORY_COMUN = "comun";

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

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(targetEntity = User.class, mappedBy = "auctions", cascade = CascadeType.ALL)
    private List<User> users;

    public Auction(CreateAuctionRequest request) {
        this.title = request.getTitle();
        this.category = request.getCategory();
        this.status = STATUS_PENDING;
        this.category = CATEGORY_COMUN;
    }
}
