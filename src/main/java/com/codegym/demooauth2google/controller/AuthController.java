package com.codegym.demooauth2google.controller;

import com.codegym.demooauth2google.dto.LoginGoogleRequest;
import com.codegym.demooauth2google.dto.ResponseDto;
import com.codegym.demooauth2google.dto.UserDto;
import com.codegym.demooauth2google.entity.User;
import com.codegym.demooauth2google.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> doLogin(@RequestBody UserDto userDto) {
        Optional<User> oldUser = userService.findByUsername(userDto.getUsername());
        return oldUser.map(
                        value -> new ResponseEntity<>(ResponseDto
                                .builder()
                                .data(value)
                                .message("Success")
                                .build(), HttpStatus.OK)
                )
                .orElseGet(
                        () -> new ResponseEntity<>(ResponseDto
                                .builder()
                                .message("User not exist !")
                                .build(), HttpStatus.NOT_MODIFIED)
                );
    }

    @PostMapping("/google-login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginGoogleRequest request) {
        ResponseDto responseDto = userService.loginGoogle(request.getCredential());
        return new ResponseEntity<>(responseDto, responseDto.getStatus());

    }

}
