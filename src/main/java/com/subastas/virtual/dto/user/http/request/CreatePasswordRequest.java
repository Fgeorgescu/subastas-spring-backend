package com.subastas.virtual.dto.user.http.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePasswordRequest {

    private String username;
    private String password;

    @JsonProperty("validation_code")
    @JsonAlias("validationCode") // Acepta ambos pero manda con validation_code
    private String validationCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CreatePasswordRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", validationCode='" + validationCode + '\'' +
                '}';
    }
}
