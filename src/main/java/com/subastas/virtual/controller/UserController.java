package com.subastas.virtual.controller;

import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.dto.user.http.request.CreatePasswordRequest;
import com.subastas.virtual.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest request) {
        log.info("Creando usuario con parametros {}", request);

        return ResponseEntity.ok().body(userService.createUser(request));
    }

    @PatchMapping(value = "/users/{id}/password")
    public ResponseEntity updatePassword(@PathVariable("id") int id, @RequestBody CreatePasswordRequest request) {
        log.info("Actualizando contraseña del usuario con parámetros {}", request);

        userService.updatePasswordFirstTime(id, request.getUsername(), request.getPassword(), request.getValidationCode());

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        log.info("Procesando GET con id {}", id);
        return ResponseEntity.ok().body(userService.getUser(id));
    }
}
