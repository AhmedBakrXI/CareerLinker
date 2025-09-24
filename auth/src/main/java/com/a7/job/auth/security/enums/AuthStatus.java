package com.a7.job.auth.security.enums;

public enum AuthStatus {
    SUCCESS,
    INVALID_EMAIL,
    INVALID_PASSWORD,
    INVALID_CREDENTIALS,
    EXPIRED_ACCESS_TOKEN,
    EMAIL_ALREADY_EXISTS,
    PASSWORDS_DO_NOT_MATCH,
    INVALID_REFRESH_TOKEN,
    ;
}
