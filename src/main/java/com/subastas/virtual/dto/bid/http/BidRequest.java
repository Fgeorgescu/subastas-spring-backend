package com.subastas.virtual.dto.bid.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BidRequest {

  private float bid;

  @JsonProperty("payment_method")
  private Long paymentMethod;
}
