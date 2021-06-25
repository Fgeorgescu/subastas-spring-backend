package com.subastas.virtual.dto.session;

import com.subastas.virtual.dto.user.User;
import lombok.Data;

@Data
public class LoginResponse {

  private User user;
  private String session;

  public LoginResponse(User user, String session) {
    this.user = user;
    this.session = session;
  }
}
