package com.codegym.demooauth2google.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDtoResponse {
    private String username;
    private String email;
    private List<String> role;
    private String name;
    private String avatar;
    private String accessToken;
}
