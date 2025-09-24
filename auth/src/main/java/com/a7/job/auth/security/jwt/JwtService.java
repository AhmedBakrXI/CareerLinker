package com.a7.job.auth.security.jwt;

import com.a7.job.auth.user.dto.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.a7.job.auth.security.jwt.JwtConstants.*;

@Service
public class JwtService {
    private final JwtUtils jwtUtils;

    @Value("${spring.application.security.jwt.access-token-expiration-time}")
    private long accessTokenExpiresInMs;

    @Value("${spring.application.security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpiresInMs;

    @Autowired
    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    public String generateAccessToken(final String username, final UserInfo userInfo) {
        final Map<String, Object> claims = Map.of(
                TOKEN_TYPE, ACCESS_TOKEN_TYPE,
                USER_ID_TYPE, userInfo.getUserId()
        );
        return buildToken(username, claims, accessTokenExpiresInMs);
    }

    public String generateRefreshToken(final String username) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, REFRESH_TOKEN_TYPE);
        return buildToken(username, claims, refreshTokenExpiresInMs);
    }

    public String refreshAccessToken(final String refreshToken, final UserInfo userInfo) {
        Claims claims = jwtUtils.extractClaims(refreshToken);
        if (!REFRESH_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        return generateAccessToken(claims.getSubject(), userInfo);
    }

    public boolean validateToken(final String token, final String expectedUsername) {
        String username = jwtUtils.extractUsernameFromToken(token);
        boolean isUsernameAsExpected = expectedUsername.equals(username);
        if (!isUsernameAsExpected) {
            return false;
        }
        return !jwtUtils.isTokenExpired(token);
    }

    public String extractUsernameFromToken(final String token) {
        return jwtUtils.extractUsernameFromToken(token);
    }

    public UserInfo extractUserInfoFromToken(final String accessToken) {
        return jwtUtils.extractUserInfoFromToken(accessToken);
    }

    private String buildToken(final String username, final Map<String, Object> claims, final long tokenExpiration) {
        return jwtUtils.buildToken(username, claims, tokenExpiration);
    }
}
