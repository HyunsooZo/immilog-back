package com.backend.immilog.notice.application;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.services.NoticeCreateService;
import com.backend.immilog.notice.application.services.command.NoticeCommandService;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.exception.NoticeException;
import com.backend.immilog.notice.presentation.request.NoticeRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("NoticeRegisterService 테스트")
class NoticeCreateServiceTest {
    private final NoticeCommandService noticeCommandService = mock(NoticeCommandService.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final NoticeCreateService noticeRegisterService = new NoticeCreateService(
            noticeCommandService,
            tokenProvider
    );

    @Test
    @DisplayName("공지사항 등록 - 성공")
    void registerNotice() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_ADMIN;
        NoticeRegisterRequest param =  new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        when(tokenProvider.getIdFromToken("token")).thenReturn(userSeq);
        // when
        noticeRegisterService.registerNotice("token", param.toCommand());
        // then
        verify(noticeCommandService, times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항 등록 - 실패: 관리자가 아닌 경우")
    void registerNotice_notAnAdminUser() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_USER;
        NoticeRegisterRequest param =  new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        // when & then
        Assertions.assertThatThrownBy(
                        () -> noticeRegisterService.registerNotice("token", param.toCommand())
                )
                .isInstanceOf(NoticeException.class)
                .hasMessage(NOT_AN_ADMIN_USER.getMessage());
    }

    @Test
    @DisplayName("공지사항 등록 - 도메인 언어 및 애그리게이트 테스트")
    void registerNotice_domainLanguageAndAggregate() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_ADMIN;
        NoticeRegisterRequest param = new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        when(tokenProvider.getIdFromToken("token")).thenReturn(userSeq);

        // when
        noticeRegisterService.registerNotice("token", param.toCommand());

        // then
        ArgumentCaptor<Notice> noticeCaptor = ArgumentCaptor.forClass(Notice.class);
        verify(noticeCommandService, times(1)).save(noticeCaptor.capture());
        Notice savedNotice = noticeCaptor.getValue();
        assertThat(savedNotice.getTitle()).isEqualTo(title);
        assertThat(savedNotice.getContent()).isEqualTo(content);
        assertThat(savedNotice.getUserSeq()).isEqualTo(userSeq);
    }

    @Test
    @DisplayName("공지사항 등록 - 유효성 검사 실패")
    void registerNotice_validationFailure() {
        // given
        Long userSeq = 1L;
        String title = "";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_ADMIN;
        NoticeRegisterRequest param = new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        when(tokenProvider.getIdFromToken("token")).thenReturn(userSeq);

        // when & then
        Assertions.assertThatThrownBy(
                        () -> noticeRegisterService.registerNotice("token", param.toCommand())
                )
                .isInstanceOf(NoticeException.class)
                .hasMessage("Title cannot be empty");
    }

    @Test
    @DisplayName("공지사항 등록 - 중복된 제목")
    void registerNotice_duplicateTitle() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_ADMIN;
        NoticeRegisterRequest param = new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        when(tokenProvider.getIdFromToken("token")).thenReturn(userSeq);
        when(noticeCommandService.isTitleDuplicate(title)).thenReturn(true);

        // when & then
        Assertions.assertThatThrownBy(
                        () -> noticeRegisterService.registerNotice("token", param.toCommand())
                )
                .isInstanceOf(NoticeException.class)
                .hasMessage("Title already exists");
    }
}
