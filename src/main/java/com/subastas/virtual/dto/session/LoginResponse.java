package com.subastas.virtual.dto.session;

import com.subastas.virtual.dto.user.UserInformation;
import lombok.Data;

@Data
public class LoginResponse {

  private UserInformation userInformation;
  private String session;

  public LoginResponse(UserInformation userInformation, String session) {
    this.userInformation = userInformation;
    this.session = session;
  }
}
