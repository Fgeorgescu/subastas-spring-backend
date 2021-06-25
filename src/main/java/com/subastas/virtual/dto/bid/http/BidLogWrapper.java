package com.subastas.virtual.dto.bid.http;

import com.subastas.virtual.dto.bid.BidLog;
import java.io.Serializable;
import lombok.Data;

@Data
public class BidLogWrapper implements Serializable {

  private Long id;
  private float bid;

  private float bid_increase;

  private String bidderUsername;

  private String itemTitle;


  public BidLogWrapper(BidLog log, String bidderUsername, String itemTitle) {
    this.id = log.getId();
    this.bid = log.getBid();
    this.bid_increase = log.getBid_increase();
    this.bidderUsername = bidderUsername;
    this.itemTitle = itemTitle;
  }
}
