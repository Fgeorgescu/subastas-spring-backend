package com.subastas.virtual.controller;

import com.subastas.virtual.SessionService;
import com.subastas.virtual.dto.session.LoginCredentials;
import com.subastas.virtual.dto.user.UserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class SessionController {

    Logger log = LoggerFactory.getLogger(SessionController.class);

    SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials creds, HttpSession session) {
        try {
            UserInformation userInformation = sessionService.validateCredentials(creds);

            sessionService.login(session, userInformation);

            return ResponseEntity.ok(userInformation);

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        sessionService.logout(session);
        return ResponseEntity.noContent().build();
    }
}
