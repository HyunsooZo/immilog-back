package com.backend.immilog.notice.application.services;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.dto.NoticeModifyCommand;
import com.backend.immilog.notice.application.usecase.NoticeModifyUseCase;
import com.backend.immilog.notice.domain.Notice;
import com.backend.immilog.notice.domain.NoticeStatus;
import com.backend.immilog.notice.domain.NoticeType;
import com.backend.immilog.notice.domain.NoticeAuthPolicy;
import com.backend.immilog.notice.exception.NoticeErrorCode;
import com.backend.immilog.notice.exception.NoticeException;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("공지사항 수정 서비스 테스트")
class NoticeModifyUseCaseTest {
    private final NoticeQueryService noticeQueryService = mock(NoticeQueryService.class);
    private final NoticeCommandService noticeCommandService = mock(NoticeCommandService.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final NoticeAuthPolicy noticeAuthPolicy = new NoticeAuthPolicy(tokenProvider);
    private final NoticeModifyUseCase noticeModifyUseCase = new NoticeModifyUseCase.NoticeModifier(
            noticeQueryService,
            noticeCommandService,
            noticeAuthPolicy
    );

    @Test
    @DisplayName("관리자가 아닌 경우 예외 발생")
    void modifyNotice_throwsExceptionIfNotAdmin() {
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = mock(NoticeModifyCommand.class);
        User user = mock(User.class);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_USER);
        when(tokenProvider.getIdFromToken(token)).thenReturn(userSeq);
        when(user.userRole()).thenReturn(UserRole.ROLE_USER);
        assertThrows(NoticeException.class, () -> noticeModifyUseCase.modifyNotice(token, noticeSeq, command));
    }

    @Test
    @DisplayName("공지사항을 찾을 수 없는 경우 예외 발생")
    void modifyNotice_throwsExceptionIfNoticeNotFound() {
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = mock(NoticeModifyCommand.class);
        User user = mock(User.class);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_USER);
        when(tokenProvider.getIdFromToken(token)).thenReturn(userSeq);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenThrow(new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        assertThrows(NoticeException.class, () -> noticeModifyUseCase.modifyNotice(token, noticeSeq, command));
    }

    @Test
    @DisplayName("삭제된 공지사항인 경우 예외 발생")
    void modifyNotice_throwsExceptionIfNoticeDeleted() {
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = mock(NoticeModifyCommand.class);
        User user = mock(User.class);
        Notice notice = mock(Notice.class);
        when(user.userRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_USER);
        when(notice.status()).thenReturn(NoticeStatus.DELETED);
        assertThrows(NoticeException.class, () -> noticeModifyUseCase.modifyNotice(token, noticeSeq, command));
    }

    @Test
    @DisplayName("공지사항 수정")
    void modifyNotice_savesNoticeIfValid() {
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand(
                "title",
                "content",
                NoticeType.NOTICE,
                NoticeStatus.NORMAL
        );
        User user = new User(
                1L,
                Auth.of("email", "password"),
                UserRole.ROLE_ADMIN,
                ReportData.of(0L, Date.valueOf(LocalDate.now())),
                Profile.of("name", "image.png", Country.SOUTH_KOREA),
                Location.of(Country.SOUTH_KOREA, "seoul"),
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
        Notice notice = mock(Notice.class);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(notice.status()).thenReturn(mock(NoticeStatus.class));
        Notice updatedNotice1 = mock(Notice.class);
        Notice updatedNotice2 = mock(Notice.class);
        Notice updatedNotice3 = mock(Notice.class);
        Notice updatedNotice4 = mock(Notice.class);
        when(notice.updateTitle(anyString())).thenReturn(updatedNotice1);
        when(updatedNotice1.updateContent(anyString())).thenReturn(updatedNotice2);
        when(updatedNotice2.updateType(any(NoticeType.class))).thenReturn(updatedNotice3);
        when(updatedNotice3.updateStatus(any(NoticeStatus.class))).thenReturn(updatedNotice4);
        when(tokenProvider.getIdFromToken(token)).thenReturn(userSeq);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_ADMIN);
        noticeModifyUseCase.modifyNotice(token, noticeSeq, command);

        verify(noticeCommandService).save(updatedNotice4);
    }
}