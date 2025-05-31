package com.backend.immilog.notice.application;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.notice.application.services.NoticeCommandService;
import com.backend.immilog.notice.application.usecase.NoticeCreateUseCase;
import com.backend.immilog.notice.domain.NoticeType;
import com.backend.immilog.notice.domain.NoticeAuthPolicy;
import com.backend.immilog.notice.exception.NoticeException;
import com.backend.immilog.notice.presentation.NoticeRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.backend.immilog.notice.exception.NoticeErrorCode.NOT_AN_ADMIN_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("NoticeRegisterService 테스트")
class NoticeCreatorTest {
    private final NoticeCommandService noticeCommandService = mock(NoticeCommandService.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final NoticeAuthPolicy noticeAuthPolicy = new NoticeAuthPolicy(tokenProvider);
    private final NoticeCreateUseCase noticeCreateUseCase = new NoticeCreateUseCase.NoticeCreator(noticeCommandService, noticeAuthPolicy);

    @Test
    @DisplayName("공지사항 등록 - 성공")
    void createNotice() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_ADMIN;
        NoticeRegisterRequest param =  new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        when(tokenProvider.getIdFromToken("token")).thenReturn(userSeq);
        // when
        noticeCreateUseCase.createNotice("token", param.toCommand());
        // then
        verify(noticeCommandService, times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항 등록 - 실패: 관리자가 아닌 경우")
    void createNotice_notAnAdminUser() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        UserRole userRole = UserRole.ROLE_USER;
        NoticeRegisterRequest param =  new NoticeRegisterRequest(title, content, NoticeType.NOTICE, null);
        when(tokenProvider.getUserRoleFromToken("token")).thenReturn(userRole);
        // when & then
        Assertions.assertThatThrownBy(() -> noticeCreateUseCase.createNotice("token", param.toCommand()))
                .isInstanceOf(NoticeException.class)
                .hasMessage(NOT_AN_ADMIN_USER.getMessage());
    }
}