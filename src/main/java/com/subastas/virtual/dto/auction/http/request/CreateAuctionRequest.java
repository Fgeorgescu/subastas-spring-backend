package com.subastas.virtual.dto.auction.http.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreateAuctionRequest {

    private String title;

    private String description;

    private String category;

    @JsonProperty("item_ids")
    @JsonAlias("itemIds")
    private List<Integer> itemIds = new ArrayList<>();
}
