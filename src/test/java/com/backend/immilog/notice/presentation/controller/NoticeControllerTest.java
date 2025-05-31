package com.backend.immilog.notice.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.notice.application.dto.NoticeModelResult;
import com.backend.immilog.notice.application.usecase.NoticeCreateUseCase;
import com.backend.immilog.notice.application.usecase.NoticeFetchUseCase;
import com.backend.immilog.notice.application.usecase.NoticeModifyUseCase;
import com.backend.immilog.notice.domain.NoticeStatus;
import com.backend.immilog.notice.domain.NoticeType;
import com.backend.immilog.notice.presentation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@DisplayName("공지사항 컨트롤러 테스트")
class NoticeControllerTest {
    private final NoticeCreateUseCase noticeCreateUseCase = mock(NoticeCreateUseCase.class);
    private final NoticeFetchUseCase noticeFetchUseCase = mock(NoticeFetchUseCase.class);
    private final NoticeModifyUseCase noticeModifyUseCase = mock(NoticeModifyUseCase.class);

    private final NoticeController noticeController = new NoticeController(
            noticeCreateUseCase,
            noticeFetchUseCase,
            noticeModifyUseCase
    );

    @Test
    @DisplayName("공지사항 등록 테스트")
    void registerNotice_success() {
        // given
        Long userSeq = 1L;
        String title = "제목";
        String content = "내용";
        String token = "token";
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserRole userRole = UserRole.ROLE_ADMIN;
        when(request.getAttribute("userRole")).thenReturn(userRole);
        when(request.getAttribute("userSeq")).thenReturn(userSeq);

        NoticeRegisterRequest param = new NoticeRegisterRequest(title, content, NoticeType.NOTICE, List.of(Country.SOUTH_KOREA));
        // when
        ResponseEntity<NoticeRegistrationResponse> response = noticeController.registerNotice(token, param);
        // then
        verify(noticeCreateUseCase).createNotice(token, param.toCommand());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test
    @DisplayName("공지사항 목록 조회 테스트")
    void getNotices_success() {
        // given
        Long userSeq = 1L;
        Integer page = 0;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(userSeq);
        Page<NoticeModelResult> results = mock(Page.class);
        when(results.isEmpty()).thenReturn(false);
        when(noticeFetchUseCase.getNotices(userSeq, page)).thenReturn(results);

        // when
        ResponseEntity<NoticeListResponse> response = noticeController.getNotices(userSeq,page);

        // then
        verify(noticeFetchUseCase).getNotices(userSeq, page);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("공지사항 상세 조회 테스트")
    void getNoticeDetail_success() {
        // given
        Long noticeSeq = 1L;
        when(noticeFetchUseCase.getNoticeDetail(noticeSeq)).thenReturn(mock(NoticeModelResult.class));

        // when
        ResponseEntity<NoticeDetailResponse> response = noticeController.getNoticeDetail(noticeSeq);

        // then
        verify(noticeFetchUseCase).getNoticeDetail(noticeSeq);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("안읽은 공지사항 여부 체크 테스트")
    void isNoticeExist_success() {
        // given
        Long userSeq = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(userSeq);
        when(noticeFetchUseCase.isUnreadNoticeExist(userSeq)).thenReturn(true);

        // when
        ResponseEntity<NoticeRegistrationResponse> response = noticeController.isNoticeExist(userSeq);

        // then
        verify(noticeFetchUseCase).isUnreadNoticeExist(userSeq);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    void modifyNotice_success() {
        // given
        Long userSeq = 1L;
        Long noticeSeq = 1L;
        String token = "token";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(userSeq);
        NoticeModifyRequest param = new NoticeModifyRequest(
                "제목",
                "내용",
                NoticeType.NOTICE,
                NoticeStatus.NORMAL
        );
        // when
        ResponseEntity<Void> response = noticeController.modifyNotice(token, noticeSeq, param);

        // then
        verify(noticeModifyUseCase).modifyNotice(token, noticeSeq, param.toCommand());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}