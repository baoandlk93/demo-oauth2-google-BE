package com.codegym.demooauth2google.service;

import com.codegym.demooauth2google.dto.request.RoleDtoRequest;
import com.codegym.demooauth2google.entity.person.Role;

public interface IRoleService {
     boolean save(RoleDtoRequest role);
}
