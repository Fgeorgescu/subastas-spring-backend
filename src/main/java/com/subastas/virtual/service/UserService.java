package com.subastas.virtual.service;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.bid.BidLog;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.payment.PaymentMethod;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.dto.user.http.UserRegistrationRequest;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.exception.custom.RequestConflictException;
import com.subastas.virtual.exception.custom.UserAlreadyExistsException;
import com.subastas.virtual.repository.BidRepository;
import com.subastas.virtual.repository.ItemRepository;
import com.subastas.virtual.repository.PaymentMethodRepository;
import com.subastas.virtual.repository.UserInformationRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  PaymentMethodRepository paymentMethodRepository;
  private final ItemRepository itemRepository;
  private final BidRepository bidRepository;
  private final JavaMailSender emailSender;

  private static final String STATUS_ACTIVE_USER = "active";

  public UserService(UserInformationRepository userRepository,
                     PaymentMethodRepository paymentMethodRepository,
                     ItemRepository itemRepository, BidRepository bidRepository, JavaMailSender emailSender) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.bidRepository = bidRepository;
    this.emailSender = emailSender;
    this.paymentMethodRepository = paymentMethodRepository;
  }

  public User createUser(UserRegistrationRequest request) {
    // TODO: Validar unicidad del nombre de usuario
    // Todo: Debería ser una transacción
    User user = new User(request);

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

  public User getUser(int id) {
    //se puede agregar que sea el user o un admin.
    log.info("Buscando usuario con id {}", id);
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id));

  }

  public User updatePassword(int id, String password) {
    log.info("Buscando usuario con id {}", id);

    User user = userRepository.findById(id)
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

  public User updateUser(int id, User newUser) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id));

    user.update(newUser);

    return userRepository.save(user);
  }

  public void addAuction(Auction auction, int id) {
    User user = getUser(id);
    if (user.getAuctions().stream().anyMatch(a -> a.getId() == auction.getId())) {
      throw new RequestConflictException("User already registered in the auction");
    }

    user.getAuctions().add(auction);
    userRepository.save(user);
  }

  public List<Auction> getAuctions(int userId) {
    User actualUser = getUser(userId);

    return actualUser.getAuctions();
  }

  public List<Auction> getAuctions(int userId, String status) {

    return getAuctions(userId).stream()
        .filter(a -> a.getStatus().equalsIgnoreCase(status))
        .collect(Collectors.toList());
  }

  public List<BidLog> getBidsFotItem(int userId, int itemId) {
    Item item = getUser(userId).getItems().stream()
        .filter(i -> i.getId() == itemId).findFirst()
        .orElseThrow(() -> new NotFoundException("Could not find item in user history"));

    // Si sacamos este .filter devolvemos toda la historia
    //return item.getBiddings().stream().filter(bid -> bid.getBidder() == userId).collect(Collectors.toList());
    return item.getBiddings();
  }

  public User setPaymentMethod(PaymentMethod paymentMethod, int userId) {
    paymentMethod.setOwner(userId);
    paymentMethodRepository.save(paymentMethod);
    return getUser(userId);
  }

  public List<PaymentMethod> getPaymentInfo(int userId) {
    return getUser(userId).getPaymentMethods();
  }

  public List<PaymentMethod> getPaymentInfo(int userId, String status) {
    return getUser(userId)
        .getPaymentMethods()
        .stream().filter(p -> status.equalsIgnoreCase(p.getStatus()))
        .collect(Collectors.toList());
  }


  public void deletePayment(int paymentId) {
    paymentMethodRepository.deleteById(paymentId);
  }

  public PaymentMethod getPaymentInfoById(int paymentId) {
    return paymentMethodRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("payment method", paymentId));
  }

  public Map<String, Object> getUserAnalytics(int userId) {
    User user = getUser(userId);
    int items = 0;

    for (Auction a : user.getAuctions()) {
      for (Item i : a.getItems()) {
        items++;
      }
    }

    Map<String, Object> analytics = new HashMap<>();
    analytics.put("auctions_participated", user.getAuctions().size());
    analytics.put("items_participated", items);
    analytics.put("items_won", itemRepository.countAllByWinnerId(userId));
    analytics.put("total_bids", bidRepository.countBidLogsByBidder(userId));

    return analytics;
  }
}
