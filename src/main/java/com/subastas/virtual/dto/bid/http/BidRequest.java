package com.subastas.virtual.dto.bid.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BidRequest {

  private float bid;
}
