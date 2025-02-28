package com.backend.immilog.notice.application;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.command.NoticeModifyCommand;
import com.backend.immilog.notice.application.services.NoticeModifyService;
import com.backend.immilog.notice.application.services.command.NoticeCommandService;
import com.backend.immilog.notice.application.services.query.NoticeQueryService;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.exception.NoticeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("NoticeModifyService 테스트")
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
    @DisplayName("공지사항 수정 - 성공")
    void modifyNotice() {
        // given
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand("newTitle", "newContent", NoticeType.NOTICE, NoticeStatus.ACTIVE);
        Notice notice = mock(Notice.class);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_ADMIN);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(notice.updateTitle(command.title())).thenReturn(notice);
        when(notice.updateContent(command.content())).thenReturn(notice);
        when(notice.updateType(command.type())).thenReturn(notice);
        when(notice.updateStatus(command.status())).thenReturn(notice);

        // when
        noticeModifyService.modifyNotice(token, noticeSeq, command);

        // then
        verify(noticeCommandService, times(1)).save(notice);
    }

    @Test
    @DisplayName("공지사항 수정 - 실패: 관리자가 아닌 경우")
    void modifyNotice_notAnAdminUser() {
        // given
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand("newTitle", "newContent", NoticeType.NOTICE, NoticeStatus.ACTIVE);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_USER);

        // when & then
        assertThatThrownBy(() -> noticeModifyService.modifyNotice(token, noticeSeq, command))
                .isInstanceOf(NoticeException.class)
                .hasMessage(NOT_AN_ADMIN_USER.getMessage());
    }

    @Test
    @DisplayName("공지사항 읽음 처리 - 성공")
    void readNotice() {
        // given
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        Notice notice = mock(Notice.class);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(notice.readByUser(userSeq)).thenReturn(notice);

        // when
        noticeModifyService.readNotice(userSeq, noticeSeq);

        // then
        verify(noticeCommandService, times(1)).save(notice);
    }

    @Test
    @DisplayName("공지사항 수정 - 도메인 언어 및 애그리게이트 테스트")
    void modifyNotice_domainLanguageAndAggregate() {
        // given
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand("newTitle", "newContent", NoticeType.NOTICE, NoticeStatus.ACTIVE);
        Notice notice = mock(Notice.class);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_ADMIN);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(notice.updateTitle(command.title())).thenReturn(notice);
        when(notice.updateContent(command.content())).thenReturn(notice);
        when(notice.updateType(command.type())).thenReturn(notice);
        when(notice.updateStatus(command.status())).thenReturn(notice);

        // when
        noticeModifyService.modifyNotice(token, noticeSeq, command);

        // then
        ArgumentCaptor<Notice> noticeCaptor = ArgumentCaptor.forClass(Notice.class);
        verify(noticeCommandService, times(1)).save(noticeCaptor.capture());
        Notice savedNotice = noticeCaptor.getValue();
        assertThat(savedNotice.getTitle()).isEqualTo(command.title());
        assertThat(savedNotice.getContent()).isEqualTo(command.content());
        assertThat(savedNotice.getType()).isEqualTo(command.type());
        assertThat(savedNotice.getStatus()).isEqualTo(command.status());
    }

    @Test
    @DisplayName("공지사항 수정 - 유효성 검사 실패")
    void modifyNotice_validationFailure() {
        // given
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand("", "newContent", NoticeType.NOTICE, NoticeStatus.ACTIVE);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_ADMIN);
        Notice notice = mock(Notice.class);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);

        // when & then
        assertThatThrownBy(() -> noticeModifyService.modifyNotice(token, noticeSeq, command))
                .isInstanceOf(NoticeException.class)
                .hasMessage("Title cannot be empty");
    }

    @Test
    @DisplayName("공지사항 수정 - 중복된 제목")
    void modifyNotice_duplicateTitle() {
        // given
        Long noticeSeq = 1L;
        String token = "token";
        NoticeModifyCommand command = new NoticeModifyCommand("newTitle", "newContent", NoticeType.NOTICE, NoticeStatus.ACTIVE);
        when(tokenProvider.getUserRoleFromToken(token)).thenReturn(UserRole.ROLE_ADMIN);
        Notice notice = mock(Notice.class);
        when(noticeQueryService.getNoticeBySeq(noticeSeq)).thenReturn(notice);
        when(noticeCommandService.isTitleDuplicate(command.title())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> noticeModifyService.modifyNotice(token, noticeSeq, command))
                .isInstanceOf(NoticeException.class)
                .hasMessage("Title already exists");
    }
}
