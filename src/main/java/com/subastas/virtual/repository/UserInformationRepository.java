package com.subastas.virtual.repository;

import com.subastas.virtual.dto.user.UserInformation;
import org.springframework.data.repository.CrudRepository;

public interface UserInformationRepository extends CrudRepository<UserInformation, Integer> {
    UserInformation findByName(String name); // Name hace referencia al nombre de usuario
}
