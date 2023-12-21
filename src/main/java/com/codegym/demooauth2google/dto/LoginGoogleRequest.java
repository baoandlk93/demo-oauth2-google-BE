package com.codegym.demooauth2google.dto;

import lombok.Data;

@Data
public class LoginGoogleRequest {
    private String clientId;
    private String credential;
    private String selectBy;
}
