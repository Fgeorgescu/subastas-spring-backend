package com.subastas.virtual.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.User;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "items")
public class RegisteredItem {

    public static final String STATUS_PROCESSING = "PROCESSING";

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

    public RegisteredItem(RegisterItemRequest request, User user) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.imageUrls = request.getImageUrls();
        this.status = STATUS_PROCESSING;
        this.owner = user.getId();
    }

}
