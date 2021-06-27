package com.subastas.virtual.repository;

import com.subastas.virtual.dto.payment.PaymentMethod;
import com.subastas.virtual.dto.user.User;
import org.springframework.data.repository.CrudRepository;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {
}
