package com.subastas.virtual.service;

import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.repository.UserInformationRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    UserInformationRepository userRepository;

    public UserService(UserInformationRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInformation createUser(UserRegistrationRequest request) {
        //Validar unicidad del nombre de usuario
        UserInformation user = new UserInformation(request.getUsername(), request.getMail());
        user = userRepository.save(user);
        return user;
    }

    public UserInformation getUser(int id) {
        log.info("Buscando usuario con id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

    }
}
