package com.subastas.virtual;

import com.subastas.virtual.dto.session.LoginCredentials;
import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.repository.UserInformationRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    UserInformationRepository userInformationRepository;

    public SessionService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    public UserInformation validateCredentials(LoginCredentials creds) {
        UserInformation aux = userInformationRepository.findByUsername(creds.getUsername()).orElseThrow();

        if (aux.getPassword() == null) {
            throw new RuntimeException("Account not validated");
        }
        if (aux.getPassword().equals(creds.getPassword())) {
            return aux;
        }
        throw new RuntimeException("Not Authorized");
    }
}
