package com.codegym.demooauth2google.service;

import com.codegym.demooauth2google.dto.request.LoginGoogleRequest;
import com.codegym.demooauth2google.dto.request.RegisterRequestDto;
import com.codegym.demooauth2google.dto.request.UserDtoAdminRequest;
import com.codegym.demooauth2google.dto.request.UserDtoRequest;
import com.codegym.demooauth2google.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface IUserService {
    ResponseDto loginGoogle(LoginGoogleRequest request);

    ResponseDto login(UserDtoRequest userDtoRequest, HttpServletResponse response);


    void saveAdmin(UserDtoAdminRequest userDtoRequest);

    ResponseDto register(RegisterRequestDto registerRequestDto);

}
