package com.subastas.virtual.repository;

import com.subastas.virtual.dto.payment.PaymentMethod;
import com.subastas.virtual.dto.user.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {
  List<PaymentMethod> findAllByOwner(int ownerId);
}
