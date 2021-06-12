package com.subastas.virtual.dto.session;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ValidationCodeCredentials {

  private String mail;
  @JsonProperty("validation_code")
  @JsonAlias("validationCode")
  @NotNull
  private String validationCode;
}
