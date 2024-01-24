package com.codegym.demooauth2google.controller;

import com.codegym.demooauth2google.dto.request.LoginGoogleRequest;
import com.codegym.demooauth2google.dto.request.RegisterRequestDto;
import com.codegym.demooauth2google.dto.request.UserDtoRequest;
import com.codegym.demooauth2google.dto.response.ResponseDto;
import com.codegym.demooauth2google.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserDtoRequest userDtoRequest, HttpServletResponse response) {
        ResponseDto responseDto = userService.login(userDtoRequest, response);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
    @PostMapping("/google-login")
    public ResponseEntity<ResponseDto> loginGoogle(@RequestBody LoginGoogleRequest request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ResponseDto responseDto = userService.loginGoogle(request);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        ResponseDto responseDto = userService.register(registerRequestDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
