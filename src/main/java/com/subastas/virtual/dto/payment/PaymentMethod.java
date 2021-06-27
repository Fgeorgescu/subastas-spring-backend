package com.subastas.virtual.dto.payment;

import com.subastas.virtual.dto.payment.converter.PaymentMethodDataConverter;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
