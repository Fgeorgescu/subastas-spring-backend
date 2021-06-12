package com.subastas.virtual.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("user_id")
    private int id;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String mail;
    private String password;
    // @JsonIgnore Esta info no debería viajar en la respuesta, pero la enviamos en desarrollo por facilidad para validar usuarios.
    private String validationCode;
    private String status;
    private String role;
    private String category;

    public UserInformation(String username, String mail) {
        this.username = username;
        this.mail = mail;
        this.validationCode = RandomStringUtils.randomAlphabetic(5);
        this.status = "pending";
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", validationCode='" + validationCode + '\'' +
                '}';
    }
}
