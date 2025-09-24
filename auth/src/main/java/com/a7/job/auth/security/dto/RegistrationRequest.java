package com.a7.job.auth.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "firstName cannot be empty")
    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    @JsonProperty("first_name")
    private String firstName;

    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    @JsonProperty("last_name")
    private String lastName;

    @Email(message = "email should be valid")
    private String email;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6, max = 100, message = "password must be between 6 and 100 characters")
    private String password;

    @NotEmpty(message = "confirmPassword cannot be empty")
    @Size(min = 6, max = 100, message = "confirmPassword must be between 6 and 100 characters")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
