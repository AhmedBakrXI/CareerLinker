package com.a7.job.auth.validator;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public interface PasswordValidator {
    boolean comparePasswordWithHashed(String password, String hashedPassword);

    String hashPassword(String password);

    default boolean comparePasswordWithConfirmation(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}
