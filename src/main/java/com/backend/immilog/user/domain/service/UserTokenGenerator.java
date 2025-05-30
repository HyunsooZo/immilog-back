package com.backend.immilog.user.domain.service;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import org.springframework.stereotype.Component;

@Component
public class UserTokenGenerator {
    private final TokenProvider tokenProvider;

    public UserTokenGenerator(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String generate(
            Long userSeq,
            String email,
            UserRole userRole,
            Country country
    ) {
        return tokenProvider.issueAccessToken(userSeq, email, userRole, country);
    }

    public String generateRefreshToken() {
        return tokenProvider.issueRefreshToken();
    }
}
