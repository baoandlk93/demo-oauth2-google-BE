package com.codegym.demooauth2google.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class UserDto {
    private String username;
    private String email;
    private String name;
    private String password;
    private String avatar;
    private String accessToken;


}
