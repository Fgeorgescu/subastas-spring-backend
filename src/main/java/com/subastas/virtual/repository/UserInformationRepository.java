package com.subastas.virtual.repository;

import com.subastas.virtual.dto.user.UserInformation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInformationRepository extends CrudRepository<UserInformation, Integer> {
  Optional<UserInformation> findByUsername(String username); // Name hace referencia al nombre de usuario

  Optional<UserInformation>  findByMail(String mail);
}
