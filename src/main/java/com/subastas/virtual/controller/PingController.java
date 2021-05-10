package com.subastas.virtual.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class PingController {

    Logger log = LoggerFactory.getLogger(PingController.class);

    @GetMapping("/ping")
    public ResponseEntity<?> ping(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username != null) {
            log.info("Known user {}", username);
            return ResponseEntity.ok(String.format("Usuario %s. Autorizado", username));
        }
        log.info("UnKnown user");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario desconocido, no autorizado");
    }
}
