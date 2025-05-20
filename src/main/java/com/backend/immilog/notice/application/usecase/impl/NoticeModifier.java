package com.backend.immilog.notice.application.usecase.impl;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.dto.NoticeModifyCommand;
import com.backend.immilog.notice.application.services.NoticeCommandService;
import com.backend.immilog.notice.application.services.NoticeQueryService;
import com.backend.immilog.notice.application.usecase.NoticeModifyUseCase;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.exception.NoticeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;

@Service
public class NoticeModifier implements NoticeModifyUseCase {
    private final NoticeQueryService noticeQueryService;
    private final NoticeCommandService noticeCommandService;
    private final TokenProvider tokenProvider;

    public NoticeModifier(
            NoticeQueryService noticeQueryService,
            NoticeCommandService noticeCommandService,
            TokenProvider tokenProvider
    ) {
        this.noticeQueryService = noticeQueryService;
        this.noticeCommandService = noticeCommandService;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public void modifyNotice(
            String token,
            Long noticeSeq,
            NoticeModifyCommand command
    ) {
        validateAdmin(token);
        Notice notice = getNoticeBySeq(noticeSeq);
        Notice updatedNotice = notice.updateTitle(command.title())
                .updateContent(command.content())
                .updateType(command.type())
                .updateStatus(command.status());
        noticeCommandService.save(updatedNotice);
    }

    private void validateAdmin(String token) {
        Optional.ofNullable(tokenProvider.getUserRoleFromToken(token))
                .filter(role -> role.equals(UserRole.ROLE_ADMIN))
                .orElseThrow(() -> new NoticeException(NOT_AN_ADMIN_USER));
    }

    private Notice getNoticeBySeq(Long noticeSeq) {
        return noticeQueryService.getNoticeBySeq(noticeSeq);
    }

    @Transactional
    public void readNotice(
            Long userSeq,
            Long noticeSeq
    ) {
        Notice notice = getNoticeBySeq(noticeSeq);
        Notice updatedNotice = notice.readByUser(userSeq);
        noticeCommandService.save(updatedNotice);
    }
}
