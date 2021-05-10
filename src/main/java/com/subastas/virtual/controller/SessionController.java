package com.subastas.virtual.controller;

import com.subastas.virtual.SessionService;
import com.subastas.virtual.dto.session.LoginCredentials;
import com.subastas.virtual.dto.user.UserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
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

            session.setAttribute("username", userInformation.getName());
            session.setAttribute("user", userInformation);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        log.info("Logging out user {}", session.getAttribute("user"));
        session.removeAttribute("username");
        session.removeAttribute("user");
        log.info("Logged out, redirecting");
        return "redirect:/ping";
    }
}
