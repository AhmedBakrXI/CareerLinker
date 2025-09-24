package com.a7.job.auth.validator.impl;

import com.a7.job.auth.validator.PasswordValidator;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidatorImpl implements PasswordValidator {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordValidatorImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean comparePasswordWithHashed(@NotNull String password, @NotNull String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
