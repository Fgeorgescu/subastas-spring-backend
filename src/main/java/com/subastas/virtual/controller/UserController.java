package com.subastas.virtual.controller;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.dto.user.http.request.CreatePasswordRequest;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.service.SessionService;
import com.subastas.virtual.service.UserService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpSession;
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

        User user = userService.createUser(request);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @PatchMapping(value = "/{id}/password")
    public ResponseEntity updatePassword(@PathVariable("id") int id, @RequestBody CreatePasswordRequest request) {
        log.info("Actualizando contraseña del usuario con parámetros {}", request);

        // TODO: Agregar validación del usuario con el ID y la sesión.

        User user = userService.updatePassword(id, request.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        log.info("Procesando GET con id {}", id);
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
                                        @RequestBody User user,
                                        HttpSession httpSession) {
        User originalUser = SessionService.getUser(httpSession);

        if (originalUser.getId() != id) {
            log.error("User {} not authorized to edit user {}", id, originalUser.getId());
            throw new UnauthorizedException("User not authorized to do this action");
        }

        log.info("Actualizando info del usuario {} con la data {}", id, user);
        return ResponseEntity.ok().body(userService.updateUser(id, user));
    }

    @GetMapping("/{id}/auctions")
    public ResponseEntity<List<Auction>> getAuctions(@PathVariable("id") int userId, HttpSession session) {
        User userSession = SessionService.getUser(session);

        if (userId != userSession.getId()) {
            throw new UnauthorizedException("User can not access this resource");
        }

        List<Auction> auctions = userService.getAuctions(userId);
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/{userId}/items/{itemId}/bids")
    public ResponseEntity<List<BidLog>> getBidsForItem(@PathVariable("userId") int userId,
                                                       @PathVariable("itemId") int itemId,
                                                       HttpSession session) {
        User userSession = SessionService.getUser(session);
        if (userId != userSession.getId()) {
            throw new UnauthorizedException("User can not access this resource");
        }

        List<BidLog> logs = userService.getBidsFotItem(userId, itemId);

        return ResponseEntity.ok(logs);
    }
}
