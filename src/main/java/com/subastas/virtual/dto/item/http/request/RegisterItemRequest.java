package com.subastas.virtual.dto.item.http.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterItemRequest {

    private int id;

    private String title;
    private String description;
    private String history;

    @JsonProperty("image_urls")
    @JsonAlias("imageUrl")
    private List<String> imageUrls;
}
