package com.subastas.virtual.controller;

import com.subastas.virtual.service.SessionService;
import com.subastas.virtual.dto.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class PingController {

    Logger log = LoggerFactory.getLogger(PingController.class);
    SessionService sessionService;

    public PingController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping(HttpSession session) {
        User user = sessionService.getUser(session);

        return ResponseEntity.ok(String.format("Usuario %s. Pong :D", user.getUsername()));
    }
}
