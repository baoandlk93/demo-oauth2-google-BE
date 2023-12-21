package com.codegym.demooauth2google.service;

import com.codegym.demooauth2google.dto.ResponseDto;
import com.codegym.demooauth2google.entity.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    ResponseDto loginGoogle(String token);
}
