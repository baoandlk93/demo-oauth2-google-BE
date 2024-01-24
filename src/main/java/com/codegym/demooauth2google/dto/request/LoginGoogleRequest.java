package com.codegym.demooauth2google.dto.request;

import lombok.Data;

@Data
public class LoginGoogleRequest {
    private String clientId;
    private String credential;
    private String selectBy;
}
