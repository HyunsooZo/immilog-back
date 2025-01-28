package com.backend.immilog.notice.application.services;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.command.NoticeModifyCommand;
import com.backend.immilog.notice.application.services.command.NoticeCommandService;
import com.backend.immilog.notice.application.services.query.NoticeQueryService;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.exception.NoticeException;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("공지사항 수정 서비스 테스트")
class NoticeModifyServiceTest {
    private final NoticeQueryService noticeQueryService = mock(NoticeQueryService.class);
    private final NoticeCommandService noticeCommandService = mock(NoticeCommandService.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final NoticeModifyService noticeModifyService = new NoticeModifyService(
            noticeQueryService,
            noticeCommandService,
            tokenProvider
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
        when(user.getUserRole()).thenReturn(UserRole.ROLE_USER);
        assertThrows(NoticeException.class, () -> noticeModifyService.modifyNotice(token, noticeSeq, command));
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
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(Optional.empty());

        assertThrows(NoticeException.class, () -> noticeModifyService.modifyNotice(token, noticeSeq, command));
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
        when(user.getUserRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(Optional.of(notice));
        when(notice.getStatus()).thenReturn(NoticeStatus.DELETED);
        assertThrows(NoticeException.class, () -> noticeModifyService.modifyNotice(token, noticeSeq, command));
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
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("email", "password"))
                .profile(Profile.of("name", "image.png", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_ADMIN)
                .location(Location.of(UserCountry.SOUTH_KOREA, "seoul"))
                .reportData(ReportData.of(0L, Date.valueOf(LocalDate.now())))
                .updatedAt(LocalDateTime.now())
                .build();
        Notice notice = mock(Notice.class);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(Optional.of(notice));
        when(notice.getStatus()).thenReturn(mock(NoticeStatus.class));
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
        noticeModifyService.modifyNotice(token, noticeSeq, command);

        verify(noticeCommandService).save(updatedNotice4);
    }
}