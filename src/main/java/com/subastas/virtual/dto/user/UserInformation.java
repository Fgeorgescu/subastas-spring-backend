package com.subastas.virtual.dto.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String mail;
    private String password;
    private String validationCode = "123";
    private String status;

    public UserInformation(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public UserInformation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", validationCode='" + validationCode + '\'' +
                '}';
    }
}
