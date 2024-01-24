package com.codegym.demooauth2google.repository;

import com.codegym.demooauth2google.entity.person.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true,
            value = "SELECT r.name  FROM role r " +
                    "JOIN user_role ur ON ur.role_id = r.id " +
                    "JOIN user u ON ur.user_id = u.id " +
                    "WHERE u.username = :username")
    List<String> findRolesByUsername(@Param("username") String username);
}
