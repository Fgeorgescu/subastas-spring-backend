package com.subastas.virtual.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Columns;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String mail;
    private String password;
    private String validationCode;
    private String status;

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
