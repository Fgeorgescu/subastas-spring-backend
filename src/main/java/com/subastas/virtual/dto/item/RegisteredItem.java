package com.subastas.virtual.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.UserInformation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "items")
public class RegisteredItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String title;

    @Column
    private String description;

    @Column
    private int auction;

    @ElementCollection
    @CollectionTable(name="imageUrls")
    private List<String> imageUrls;

    public RegisteredItem(RegisterItemRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.imageUrls = request.getImageUrls();
    }

}
