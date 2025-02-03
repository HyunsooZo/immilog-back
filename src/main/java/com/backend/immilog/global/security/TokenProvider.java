package com.backend.immilog.global.security;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    void init();

    String issueAccessToken(
            Long id,
            String email,
            UserRole userRole,
            Country country
    );

    String issueRefreshToken();

    boolean validateToken(String token);

    Long getIdFromToken(String token);

    String getEmailFromToken(String token);

    Authentication getAuthentication(String token);

    UserRole getUserRoleFromToken(String authorizationHeader);
}
