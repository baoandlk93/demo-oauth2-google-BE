package com.codegym.demooauth2google.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;

}
