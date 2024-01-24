package com.codegym.demooauth2google.service.impl;

import com.codegym.demooauth2google.dto.request.RoleDtoRequest;
import com.codegym.demooauth2google.entity.person.Role;
import com.codegym.demooauth2google.repository.IRoleRepository;
import com.codegym.demooauth2google.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    public boolean save(RoleDtoRequest roleDtoRequest) {
        Role role = Role.builder()
                .description(roleDtoRequest.getDescription())
                .name(roleDtoRequest.getRole())
                .build();
        try {
            roleRepository.save(role);
            return true;
        } catch (Exception e) {
            return false;

        }
    }
}
