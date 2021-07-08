package com.subastas.virtual.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.constantes.Currency;
import com.subastas.virtual.dto.payment.converter.PaymentMethodDataConverter;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
public class PaymentMethod {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private PaymentMethodType type;

  private String name;

  @Column
  private int owner;

  @Convert(converter = PaymentMethodDataConverter.class)
  private Map<String, Object> data;

  @OneToMany(mappedBy = "paymentId", fetch = FetchType.EAGER)
  @JsonIgnore
  @ToString.Exclude
  private List<BidLog> bids;

  @Column(name = "status")
  private String status = "PENDING_REVIEW";

  @Column(columnDefinition = "int default 0")
  @Enumerated
  private Currency currency = Currency.PESO;
}
