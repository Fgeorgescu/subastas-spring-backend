package com.subastas.virtual;

import com.subastas.virtual.dto.session.LoginCredentials;
import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.exception.custom.UnauthorizedException;
import com.subastas.virtual.repository.UserInformationRepository;
import java.util.Arrays;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private static final String USERNAME_KEY = "username";
    private static final String USER_KEY = "user";

    UserInformationRepository userInformationRepository;

    public SessionService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    public UserInformation validateCredentials(LoginCredentials creds) {
        UserInformation aux = userInformationRepository.findByUsername(creds.getUsername()).orElseThrow();

        if (aux.getPassword() == null) {
            throw new UnauthorizedException("Account not validated");
        }
        if (aux.getPassword().equals(creds.getPassword())) {
            return aux;
        }
        throw new UnauthorizedException("Not Authorized");
    }

    public UserInformation getUser(HttpSession session) {
        String username = (String) session.getAttribute(USERNAME_KEY);
        UserInformation user = (UserInformation) session.getAttribute(USER_KEY);
        if (username == null) {
            throw new UnauthorizedException("No session found");
        }

        return user;
    }

    public void hasRole(HttpSession session, String... roles) {
        UserInformation user = getUser(session);
        if (Arrays.stream(roles).noneMatch(r -> r.equalsIgnoreCase(user.getRole()))) {
            throw new UnauthorizedException(
                    String.format("User %s is not authorized to perform this action",
                            user.getUsername()));
        }
    }

    public void login(HttpSession session, UserInformation userInformation) {
        session.setAttribute(USERNAME_KEY, userInformation.getUsername());
        session.setAttribute(USER_KEY, userInformation);
    }

    public void logout(HttpSession session) {
        session.invalidate();

        /*
            log.info("Logging out user {}", session.getAttribute("user"));
            session.removeAttribute("username");
            session.removeAttribute("user");
            log.info("Logged out, redirecting");
         */
    }
}
