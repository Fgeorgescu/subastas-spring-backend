package com.subastas.virtual.service;

import com.subastas.virtual.dto.user.UserInformation;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.UserAlreadyExistsException;
import com.subastas.virtual.repository.UserInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  Logger log = LoggerFactory.getLogger(UserService.class);

  UserInformationRepository userRepository;
  private final JavaMailSender emailSender;

  private static final String STATUS_ACTIVE_USER = "active";

  public UserService(UserInformationRepository userRepository, JavaMailSender emailSender) {
    this.userRepository = userRepository;
    this.emailSender = emailSender;
  }

  public UserInformation createUser(UserRegistrationRequest request) {
    // TODO: Validar unicidad del nombre de usuario
    // Todo: Debería ser una transacción
    UserInformation user = new UserInformation(request);

    try {
      user = userRepository.save(user);
      sendSimpleMessage(
          user.getMail(),
          "Bienvenido",
          String.format("Hola! Tu código de validación es \"%s\"", user.getValidationCode())
      );
    } catch (DataIntegrityViolationException e) {
      userRepository.delete(user); // Si implementamos transacciones podríamos eliminar este delete
      log.error("Error: {}", e.getMessage());

      throw new UserAlreadyExistsException("El usuario o el mail ya existen");
    }
    return user;
  }

  public UserInformation getUser(int id) {
    //se puede agregar que sea el user o un admin.
    log.info("Buscando usuario con id {}", id);
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

  }

  public UserInformation updatePassword(int id, String password) {
    log.info("Buscando usuario con id {}", id);

    UserInformation user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("user", id));

    log.info("usuario con id {}: {}", id, user);

    log.info("Valores correctos, actualizamos el usuario {} con la contraseña a {}", user, password);

    user.setPassword(password);
    user.setStatus(STATUS_ACTIVE_USER);
    log.info("Cambiamos la pass y el usuario, actualizamos el usuario {} con la contraseña a {}", user, password);
    user = userRepository.save(user);
    log.info("guardado");

    return user;
  }


  public void sendSimpleMessage(
      String to, String subject, String text) {
    if (System.getenv("GMAIL_PASSWORD") == null
        || "".equals(System.getenv("GMAIL_PASSWORD"))) {
      log.warn("NOT PASSWORD SET, MAIL WILL NOT BE SEND BUT USER WILL BE CREATED");
      // En caso de no tener la env definida no hacemos nada, no se manda el mail.
      // Lo hacemos para facilitar el desarrollo del frontend, ya que tienen que settear la pass
      // cada vez que levantan el backend
      return;
    }

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("appdistgrupo3@gmail.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }
}
