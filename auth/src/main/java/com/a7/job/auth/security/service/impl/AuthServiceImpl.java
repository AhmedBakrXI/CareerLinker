package com.a7.job.auth.security.service.impl;

import com.a7.job.auth.logging.LogProducer;
import com.a7.job.auth.security.dto.*;
import com.a7.job.auth.security.enums.AuthStatus;
import com.a7.job.auth.security.jwt.JwtService;
import com.a7.job.auth.security.service.AuthService;
import com.a7.job.auth.validator.PasswordValidator;
import com.a7.job.auth.user.dto.UserInfo;
import com.a7.job.auth.user.model.UserModel;
import com.a7.job.auth.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordValidator passwordValidator;
    private final LogProducer logProducer;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        logProducer.sendLog("Login attempt for email: " + authRequest.getEmail());
        Optional<UserModel> userModelOpt = userService.findByEmail(authRequest.getEmail());
        if (userModelOpt.isEmpty()) {
            return AuthResponse.builder()
                    .status(AuthStatus.INVALID_CREDENTIALS)
                    .build();
        }
        UserModel userModel = userModelOpt.get();
        // check password
        if (!passwordValidator.comparePasswordWithHashed(authRequest.getPassword(), userModel.getHashedPassword())) {
            return AuthResponse.builder()
                    .status(AuthStatus.INVALID_CREDENTIALS)
                    .build();
        }

        String accessToken = jwtService.generateAccessToken(userModel.getEmail(), UserInfo.builder()
                .userId(userModel.getId())
                .build());

        String refreshToken = jwtService.generateRefreshToken(userModel.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .status(AuthStatus.SUCCESS)
                .build();
    }

    @Override
    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        if (userService.existsByEmail(registrationRequest.getEmail())) {
            return RegistrationResponse.builder()
                    .status(AuthStatus.EMAIL_ALREADY_EXISTS)
                    .description("Email already exists")
                    .build();
        }
        if (!passwordValidator.comparePasswordWithConfirmation(registrationRequest.getPassword(), registrationRequest.getConfirmPassword())) {
            return RegistrationResponse.builder()
                    .status(AuthStatus.PASSWORDS_DO_NOT_MATCH)
                    .description("Passwords do not match")
                    .build();
        }
        UserModel userModel = UserModel.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .hashedPassword(passwordValidator.hashPassword(registrationRequest.getPassword()))
                .build();
        userService.saveUser(userModel);
        return RegistrationResponse.builder()
                .status(AuthStatus.SUCCESS)
                .description(registrationRequest.getFirstName() + " " + registrationRequest.getLastName())
                .build();
    }

    @Override
    public AuthResponse refresh(RefreshRequest refreshRequest) {
        if (!validateToken(refreshRequest.getRefreshToken(),refreshRequest.getEmail())) {
            return AuthResponse.builder()
                    .status(AuthStatus.INVALID_REFRESH_TOKEN)
                    .build();
        }
        Optional<UserModel> userModelOpt = userService.findByEmail(refreshRequest.getEmail());
        if (userModelOpt.isEmpty()) {
            return AuthResponse.builder()
                    .status(AuthStatus.INVALID_CREDENTIALS)
                    .build();
        }
        UserModel userModel = userModelOpt.get();
        String newAccessToken = jwtService.refreshAccessToken(refreshRequest.getRefreshToken(), UserInfo.builder()
                .userId(userModel.getId())
                .build());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshRequest.getRefreshToken())
                .status(AuthStatus.SUCCESS)
                .build();
    }

    @Override
    public boolean validateToken(String token, String email) {
        return jwtService.validateToken(token, email);
    }
    
}
