package com.subastas.virtual.controller;

import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.dto.user.http.request.CreatePasswordRequest;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.service.SessionService;
import com.subastas.virtual.service.UserService;
import java.net.URI;
import javax.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest request) {
        log.info("Creando usuario con parametros {}", request);

        UserInformation user = userService.createUser(request);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @PatchMapping(value = "/{id}/password")
    public ResponseEntity updatePassword(@PathVariable("id") int id, @RequestBody CreatePasswordRequest request) {
        log.info("Actualizando contraseña del usuario con parámetros {}", request);

        // TODO: Agregar validación del usuario con el ID y la sesión.

        UserInformation user = userService.updatePassword(id, request.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        log.info("Procesando GET con id {}", id);
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
                                        @RequestBody UserInformation userInformation,
                                        HttpSession httpSession) {
        UserInformation originalUser = SessionService.getUser(httpSession);

        if (originalUser.getId() != id) {
            log.error("User {} not authorized to edit user {}", id, originalUser.getId());
            throw new UnauthorizedException("User not authorized to do this action");
        }

        log.info("Actualizando info del usuario {} con la data {}", id, userInformation);
        return ResponseEntity.ok().body(userService.updateUser(id, userInformation));
    }
}
