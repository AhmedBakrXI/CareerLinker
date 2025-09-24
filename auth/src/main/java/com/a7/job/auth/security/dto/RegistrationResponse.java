package com.a7.job.auth.security.dto;

import com.a7.job.auth.security.enums.AuthStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private AuthStatus status;
    private String description;
}
