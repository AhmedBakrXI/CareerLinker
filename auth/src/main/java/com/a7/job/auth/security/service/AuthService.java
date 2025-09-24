package com.a7.job.auth.security.service;

import com.a7.job.auth.security.dto.*;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    RegistrationResponse register(RegistrationRequest registrationRequest);
    AuthResponse refresh(RefreshRequest refreshRequest);
    boolean validateToken(String token, String email);
}
