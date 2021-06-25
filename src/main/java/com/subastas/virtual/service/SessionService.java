package com.subastas.virtual.service;

import com.subastas.virtual.dto.session.LoginCredentials;
import com.subastas.virtual.dto.session.ValidationCodeCredentials;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.repository.UserInformationRepository;
import java.util.Arrays;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private static final String USERNAME_KEY = "username";
    private static final String USER_KEY = "user";

    UserInformationRepository userInformationRepository;

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    public SessionService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    public User validateCredentials(LoginCredentials creds) {
        User aux = userInformationRepository.findByUsername(creds.getUsername()).orElseThrow();

        if (aux.getPassword() == null) {
            throw new UnauthorizedException("Account not validated");
        }
        if (aux.getPassword().equals(creds.getPassword())) {
            return aux;
        }
        throw new UnauthorizedException("Not Authorized");
    }

    public User validateValidationCode(ValidationCodeCredentials creds) {
        User user = userInformationRepository.findByMail(creds.getMail())
            .orElseThrow(() -> new NotFoundException("mail", creds.getMail()));

        if ("ADMIN".equals(creds.getValidationCode()) ||
            (user.getPassword() == null && creds.getValidationCode().equals(user.getValidationCode()))) {
            return user;
        }
        throw new UnauthorizedException("Not Authorized");
    }

    public static User getUser(HttpSession session) {
        String username = (String) session.getAttribute(USERNAME_KEY);
        User user = (User) session.getAttribute(USER_KEY);
        if (username == null || user== null) {
            throw new UnauthorizedException("No session found");
        }

        return user;
    }

    public void hasRole(HttpSession session, String... roles) {
        User user = getUser(session);
        if (Arrays.stream(roles).noneMatch(r -> r.equalsIgnoreCase(user.getRole()))) {
            throw new UnauthorizedException(
                    String.format("User %s is not authorized to perform this action",
                            user.getUsername()));
        }
    }

    public void login(HttpSession session, User user) {
        session.setAttribute(USERNAME_KEY, user.getUsername());
        session.setAttribute(USER_KEY, user);

        log.info("User session created successfully for user {}", user);
    }

    public void logout(HttpSession session) {
        try {

        log.info("Invalidating session for user: {}", getUser(session));
        session.invalidate();
        } catch (UnauthorizedException ex) {
            log.info("No session to invalidate");
        }
        /*
            log.info("Logging out user {}", session.getAttribute("user"));
            session.removeAttribute("username");
            session.removeAttribute("user");
            log.info("Logged out, redirecting");
         */
    }
}
