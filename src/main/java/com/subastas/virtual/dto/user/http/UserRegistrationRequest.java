package com.subastas.virtual.dto.user.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserRegistrationRequest {

    private String username;
    private String mail;
    private int document;
    private String phone;
    private String address;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    @Override
    public String toString() {
        return "UserRegistrationRequest{" +
                "username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
