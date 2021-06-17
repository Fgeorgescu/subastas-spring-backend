package com.subastas.virtual.dto.user.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.subastas.virtual.dto.user.http.request.CreatePasswordRequest;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserRegistrationRequest {

    private String username;
    private String mail;
    private int document;
    private String phone;
    private String address;
}
