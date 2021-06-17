package com.subastas.virtual.dto.user.http.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CreatePasswordRequest {

    private String password;

    @JsonProperty("validation_code")
    @JsonAlias("validationCode") // Acepta ambos pero manda con validation_code
    private String validationCode;
}
