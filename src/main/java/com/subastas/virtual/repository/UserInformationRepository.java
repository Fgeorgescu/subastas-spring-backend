package com.subastas.virtual.repository;

import com.subastas.virtual.dto.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInformationRepository extends CrudRepository<User, Integer> {
  Optional<User> findByUsername(String username);

  Optional<User>  findByMail(String mail);
}
