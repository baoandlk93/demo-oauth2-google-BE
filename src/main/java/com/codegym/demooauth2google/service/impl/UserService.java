package com.codegym.demooauth2google.service.impl;

import com.codegym.demooauth2google.dto.request.LoginGoogleRequest;
import com.codegym.demooauth2google.dto.request.RegisterRequestDto;
import com.codegym.demooauth2google.dto.request.UserDtoAdminRequest;
import com.codegym.demooauth2google.dto.request.UserDtoRequest;
import com.codegym.demooauth2google.dto.response.ResponseDto;
import com.codegym.demooauth2google.dto.response.UserDtoResponse;
import com.codegym.demooauth2google.entity.person.Role;
import com.codegym.demooauth2google.entity.person.User;
import com.codegym.demooauth2google.repository.IRoleRepository;
import com.codegym.demooauth2google.repository.IUserRepository;
import com.codegym.demooauth2google.service.IUserService;
import com.codegym.demooauth2google.utillity.cache.TokenCache;
import com.codegym.demooauth2google.utillity.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenCache tokenCache;
    private final JwtTokenProvider tokenProvider;

    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private ResponseDto authenticate(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> role = Collections.singletonList(String.valueOf(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()));
        String token = tokenProvider.generateToken(authentication);
        tokenCache.addToken(tokenProvider.getUsernameFromJWT(token), token);
        return ResponseDto.builder()
                .data(
                        UserDtoResponse
                                .builder()
                                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                                .accessToken(token)
                                .role(role)
                                .build()
                )
                .message("Login success")
                .status(HttpStatus.OK)
                .build();
    }

    public ResponseDto loginGoogle(LoginGoogleRequest request) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(request.getClientId()))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(request.getCredential());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String userEmail = payload.getEmail();
            String[] username = userEmail.split("@");
            Optional<User> oldUser = findByUsername(username[0]);
            if (oldUser.isPresent()) {
                return authenticate(oldUser.get().getUsername(), oldUser.get().getPassword());
            } else {
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                Role role = roleRepository.findByName("BUYER");
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(role);
                String password = passwordEncoder.encode("123456");
                User user = User.builder()
                        .email(email)
                        .name(name)
                        .password(password)
                        .activated(emailVerified)
                        .username(username[0])
                        .avatar(pictureUrl)
                        .roles(roleSet)
                        .build();
                userRepository.save(user);
                return authenticate(user.getUsername(), user.getPassword());
            }
        } else {
            return ResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid ID token.")
                    .build();
        }


    }


    @Override
    public ResponseDto login(UserDtoRequest userDtoRequest, HttpServletResponse response) {
        return authenticate(userDtoRequest.getUsername(), userDtoRequest.getPassword());
    }

    @Override
    public void saveAdmin(UserDtoAdminRequest userDtoRequest) {
        User user = new User();
        user.setUsername(userDtoRequest.getUsername());
        String password = passwordEncoder.encode(userDtoRequest.getPassword());
        user.setPassword(password);
        user.setActivated(true);
        user.setEmail(userDtoRequest.getEmail());
        Role roleAdmin = roleRepository.findByName("ADMIN");
        Role roleBuyer = roleRepository.findByName("BUYER");
        Role roleSeller = roleRepository.findByName("SELLER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleAdmin);
        roleSet.add(roleBuyer);
        roleSet.add(roleSeller);
        user.setRoles(roleSet);
        try {
            userRepository.save(user);
        } catch (ConstraintViolationException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public ResponseDto register(RegisterRequestDto registerRequestDto) {
        Role roleSeller = roleRepository.findByName("BUYER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleSeller);
        String password = passwordEncoder.encode(registerRequestDto.getPassword());
        User user = User.builder()
                .password(password)
                .username(registerRequestDto.getUsername())
                .phone(registerRequestDto.getPhoneNumber())
                .email(registerRequestDto.getEmail())
                .roles(roleSet)
                .activated(true)
                .build();
        try {
            userRepository.save(user);
            return ResponseDto.builder()
                    .data(user)
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponseDto.builder()
                    .data(null)
                    .message("Fail try again !")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        }

    }
}
