package com.subastas.virtual.dto.item;

import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.dto.user.UserInformation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
public class RegisteredItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private String description;

    @Column
    private int auction;

    @ElementCollection
    @CollectionTable(name="photoIds")
    private List<Integer> photoIds = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private UserInformation owner;

    public RegisteredItem(RegisterItemRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

}
