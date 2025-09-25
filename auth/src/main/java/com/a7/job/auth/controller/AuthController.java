package com.a7.job.auth.controller;

import com.a7.job.auth.security.dto.*;
import com.a7.job.auth.security.enums.AuthStatus;
import com.a7.job.auth.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse response = authService.register(registrationRequest);
        if (response.getStatus() != AuthStatus.SUCCESS) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        if (response.getStatus() != AuthStatus.SUCCESS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<Boolean> verifyToken(@RequestParam("token") String token, @RequestParam("email") String email) {
        boolean isValid = authService.validateToken(token, email);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestParam RefreshRequest refreshToken) {
        AuthResponse response = authService.refresh(refreshToken);
        if (response.getStatus() != AuthStatus.SUCCESS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
