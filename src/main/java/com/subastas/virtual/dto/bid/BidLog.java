package com.subastas.virtual.dto.bid;

import com.subastas.virtual.dto.payment.PaymentMethod;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bids")
public class BidLog implements Comparable<BidLog> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private float bid;

  @Column
  private float bid_increase;

  @Column(name = "bidderId")
  private int bidder;

  @Column(name = "itemId")
  private int itemId;

  @Column(name = "payment_id")
  private int paymentId;

  public BidLog(float bid, float bid_increase, int bidder, int item) {
    this.bid = bid;
    this.bid_increase = bid_increase;
    this.bidder = bidder;
    this.itemId = item;
  }

  @Override
  public int compareTo(BidLog o) {
    return (int) (this.getId() - o.getId());
  }
}
