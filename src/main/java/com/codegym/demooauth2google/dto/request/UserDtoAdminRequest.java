package com.codegym.demooauth2google.dto.request;

import lombok.Data;

@Data
public class UserDtoAdminRequest {
    private String username;
    private String password;
    private String email;
}
