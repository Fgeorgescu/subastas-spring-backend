package com.subastas.virtual.dto.item.http.request;

import lombok.Data;

@Data
public class RegisterItemRequest {

    private int id;

    private String title;
    private String description;
}
