package com.codegym.demooauth2google.repository;

import com.codegym.demooauth2google.entity.person.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
