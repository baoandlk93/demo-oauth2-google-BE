package com.codegym.demooauth2google.controller;

import com.codegym.demooauth2google.dto.request.RoleDtoRequest;
import com.codegym.demooauth2google.dto.request.UserDtoAdminRequest;
import com.codegym.demooauth2google.dto.response.ResponseDto;
import com.codegym.demooauth2google.service.IRoleService;
import com.codegym.demooauth2google.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IRoleService roleService;
    private final IUserService userService;

    @PostMapping("/role/add")
    public ResponseEntity<ResponseDto> addNewRole(@RequestBody RoleDtoRequest roleDto) {
        roleService.save(roleDto);
        return new ResponseEntity<>(ResponseDto.builder().build(), HttpStatus.OK);
    }
    @PostMapping("/user/add")
    public ResponseEntity<ResponseDto> addNewUser(@RequestBody UserDtoAdminRequest userDtoRequest) {
       userService.saveAdmin(userDtoRequest);
        return new ResponseEntity<>(ResponseDto.builder().build(), HttpStatus.OK);
    }

}
