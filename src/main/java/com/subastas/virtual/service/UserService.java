package com.subastas.virtual.service;

import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.repository.UserInformationRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    UserInformationRepository userRepository;
    private JavaMailSender emailSender;

    public UserService(UserInformationRepository userRepository, JavaMailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public UserInformation createUser(UserRegistrationRequest request) {
        // TODO: Validar unicidad del nombre de usuario
        UserInformation user = new UserInformation(request.getUsername(), request.getMail());
        user = userRepository.save(user);
        sendSimpleMessage(
                user.getMail(),
                "Bienvenido",
                String.format("Hola! Tu código de validación es \"%s\"", user.getValidationCode())
        );
        return user;
    }

    public UserInformation getUser(int id) {
        //se puede agregar que sea el user o un admin.
        log.info("Buscando usuario con id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

    }

    public void updatePasswordFirstTime(int id, String username, String password, String validationCode) {
        log.info("Buscando usuario con id {}", id);
        UserInformation user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("Not found"));
        log.info("usuario con id {}: {}", id, user);

        if (user.getPassword() == null && validationCode.equals(user.getValidationCode())) {
            log.info("Valores correctos, actualizamos el usuario {} con la contraseña a {}", user, password);

            user.setPassword(password);
            log.info("Cambiamos pa pass, actualizamos el usuario {} con la contraseña a {}", user, password);
            userRepository.save(user);
            log.info("guardado");

            return;
        }

        throw new RuntimeException("Error updating new password identity");
    }


    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("appdistgrupo3@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
