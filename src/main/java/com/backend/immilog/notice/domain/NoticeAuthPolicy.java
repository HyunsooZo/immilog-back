package com.backend.immilog.notice.domain;

import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.exception.NoticeException;
import org.springframework.stereotype.Service;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;

@Service
public class NoticeAuthPolicy {
    private final TokenProvider tokenProvider;

    public NoticeAuthPolicy(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public void validateAdmin(String token) {
        if (!tokenProvider.getUserRoleFromToken(token).isAdmin()) {
            throw new NoticeException(NOT_AN_ADMIN_USER);
        }
    }

    public Long getUserSeqFromToken(String token) {
        return tokenProvider.getIdFromToken(token);
    }
}
