package com.backend.immilog.notice.application.services;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.command.NoticeUploadCommand;
import com.backend.immilog.notice.application.services.command.NoticeCommandService;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.exception.NoticeException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;

@Service
public class NoticeCreateService {
    private final NoticeCommandService noticeCommandService;
    private final TokenProvider tokenProvider;

    public NoticeCreateService(
            NoticeCommandService noticeCommandService,
            TokenProvider tokenProvider
    ) {
        this.noticeCommandService = noticeCommandService;
        this.tokenProvider = tokenProvider;
    }

    public void registerNotice(
            String token,
            NoticeUploadCommand command
    ) {
        validateAdminUser(token);
        Long userSeq = extractUserSeq(token);
        noticeCommandService.save(Notice.of(userSeq, command));
    }

    private void validateAdminUser(String token) {
        Optional.ofNullable(tokenProvider.getUserRoleFromToken(token))
                .filter(role -> Objects.equals(role, UserRole.ROLE_ADMIN))
                .orElseThrow(() -> new NoticeException(NOT_AN_ADMIN_USER));
    }

    private Long extractUserSeq(String token) {
        return tokenProvider.getIdFromToken(token);
    }
}
