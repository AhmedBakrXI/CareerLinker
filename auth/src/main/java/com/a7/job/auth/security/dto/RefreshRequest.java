package com.a7.job.auth.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshRequest {
    private String email;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
