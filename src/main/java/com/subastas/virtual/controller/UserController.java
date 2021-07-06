package com.subastas.virtual.controller;

import com.google.common.base.Strings;
import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.payment.PaymentMethod;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.dto.user.http.request.CreatePasswordRequest;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.service.SessionService;
import com.subastas.virtual.service.UserService;
import io.swagger.annotations.SwaggerDefinition;
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
    public ResponseEntity<?> updateUser(@PathVariable("id") int userId,
                                        @RequestBody User user,
                                        HttpSession session) {
        SessionService.validateUser(session, userId);


        log.info("Actualizando info del usuario {} con la data {}", userId, user);
        return ResponseEntity.ok().body(userService.updateUser(userId, user));
    }

    @GetMapping("/{id}/auctions")
    public ResponseEntity<List<Auction>> getAuctions(@PathVariable("id") int userId,
                                                     @RequestParam(name = "status", defaultValue = "") String status,
                                                     HttpSession session) {
        SessionService.validateUser(session, userId);
        if ("".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(userService.getAuctions(userId));
        }
        return ResponseEntity.ok(userService.getAuctions(userId, status));

    }

    @GetMapping("/{userId}/items/{itemId}/bids")
    public ResponseEntity<List<BidLog>> getBidsForItem(@PathVariable("userId") int userId,
                                                       @PathVariable("itemId") int itemId,
                                                       HttpSession session) {
        SessionService.validateUser(session, userId);


        List<BidLog> logs = userService.getBidsFotItem(userId, itemId);

        return ResponseEntity.ok(logs);
    }

    @PostMapping("/{userId}/payments")
    public ResponseEntity<User> registerPaymentMethod(@PathVariable("userId") int userId,
                                                      @RequestBody PaymentMethod paymentMethod,
                                                      HttpSession session) {
        SessionService.validateUser(session, userId);

        User user = userService.setPaymentMethod(paymentMethod, userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/payments")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethod(@PathVariable("userId") int userId,
                                                      @RequestParam(value = "status", defaultValue = "") String status,
                                                      HttpSession session) {
        SessionService.validateUser(session, userId);

        if (Strings.isNullOrEmpty(status)) {
            return ResponseEntity.ok(userService.getPaymentInfo(userId));
        } else {
            return ResponseEntity.ok(userService.getPaymentInfo(userId, status));
        }
    }

    @DeleteMapping("/{userId}/payments/{paymentId}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable("userId") int userId,
                                                                @PathVariable("paymentId") int paymentId,
                                                                HttpSession session) {
        SessionService.validateUser(session, userId);
        userService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/payments/{paymentId}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable("userId") int userId,
                                                 @PathVariable("paymentId") int paymentId,
                                                 HttpSession session) {
        SessionService.validateUser(session, userId);

        return ResponseEntity.ok(userService.getPaymentInfoById(paymentId));
    }

    @GetMapping("/{userId}/analytics")
    public ResponseEntity<?> getUserAnalytics(@PathVariable("userId") int userId,
                                                  HttpSession session) {
        SessionService.validateUser(session, userId);

        return ResponseEntity.ok(userService.getUserAnalytics(userId));
    }

}
