package com.codegym.demooauth2google.service.impl;

import com.codegym.demooauth2google.dto.ResponseDto;
import com.codegym.demooauth2google.entity.User;
import com.codegym.demooauth2google.repository.IUserRepository;
import com.codegym.demooauth2google.service.IUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    @Value("${client_id}")
    private String clientId;
    private final IUserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseDto loginGoogle(String credentials) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(clientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(credentials);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String userEmail = payload.getEmail();
            Optional<User> oldUser = userRepository.findByUsername(userEmail);
            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");

            User user = User.builder()
                    .email(email)
                    .name(name)
                    .address(locale)
                    .activated(emailVerified)
                    .avatar(pictureUrl)
                    .build();
            return ResponseDto.builder()
                    .data(user)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            System.out.println("Invalid ID token.");
            return ResponseDto.builder().status(HttpStatus.BAD_REQUEST).build();
        }


    }
}
