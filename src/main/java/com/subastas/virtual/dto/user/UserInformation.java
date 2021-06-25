package com.subastas.virtual.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Columns;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    private int id;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String mail;
    // @JsonIgnore Esta info no debería viajar en la respuesta, pero la enviamos en desarrollo por facilidad para validar usuarios.
    private String password;
    // @JsonIgnore Esta info no debería viajar en la respuesta, pero la enviamos en desarrollo por facilidad para validar usuarios.
    private String validationCode;
    private String status;
    private String role;
    private String category;
    private int document;
    private String phone;
    private String address;

    // TODO: Esto debería estar en /users/{id}/items. A futuro cambiarlo y agregar @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<RegisteredItem> items;

    public UserInformation(String username, String mail) {
        this.username = username;
        this.mail = mail;
        this.validationCode = RandomStringUtils.randomAlphabetic(5);
        this.status = "pending";
    }

    public UserInformation(UserRegistrationRequest request) {
        this.username = request.getUsername();
        this.mail = request.getMail();
        this.validationCode = RandomStringUtils.randomAlphabetic(5);
        this.status = "pending";
        this.category = "CLASSIC";
        this.document = request.getDocument();
        this.phone = request.getPhone();
        this.address = request.getAddress();
    }

    public void update(UserInformation i) {
        this.status = Strings.isNullOrEmpty(i.status) ? this.status : i.status;
        this.category = Strings.isNullOrEmpty(i.category) ? this.category : i.category;
        this.phone = Strings.isNullOrEmpty(i.phone) ? this.phone : i.phone;
        this.address = Strings.isNullOrEmpty(i.address) ? this.address : i.address;
        this.password = Strings.isNullOrEmpty(i.password) ? this.password : i.password;
    }
}