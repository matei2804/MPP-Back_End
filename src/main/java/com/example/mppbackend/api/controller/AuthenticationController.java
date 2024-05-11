package com.example.mppbackend.api.controller;

import com.example.mppbackend.api.models.LoginRequest;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.TokenService;
import com.example.mppbackend.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    private final TokenService tokenService;

    public AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.checkUserLogin(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            Optional<User> user = userService.getUserDetailsByEmail(loginRequest.getEmail());
            User User = user.get();
            String token = tokenService.generateToken(User);
            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

}
