package com.backend.immilog.notice.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.notice.application.services.NoticeCreateService;
import com.backend.immilog.notice.application.services.NoticeInquiryService;
import com.backend.immilog.notice.application.services.NoticeModifyService;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.presentation.request.NoticeModifyRequest;
import com.backend.immilog.notice.presentation.request.NoticeRegisterRequest;
import com.backend.immilog.notice.presentation.response.NoticeApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("공지사항 컨트롤러 테스트")
class NoticeControllerTest {
    private final NoticeCreateService noticeRegisterService = mock(NoticeCreateService.class);
    private final NoticeInquiryService noticeInquiryService = mock(NoticeInquiryService.class);
    private final NoticeModifyService noticeModifyService = mock(NoticeModifyService.class);

    private final NoticeController noticeController = new NoticeController(
            noticeRegisterService,
            noticeInquiryService,
            noticeModifyService
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
        ResponseEntity<NoticeApiResponse> response = noticeController.registerNotice(token, param);
        // then
        verify(noticeRegisterService).registerNotice(token, param.toCommand());
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
        when(noticeInquiryService.getNotices(userSeq, page)).thenReturn(null);

        // when
        ResponseEntity<NoticeApiResponse> response = noticeController.getNotices(userSeq,page);

        // then
        verify(noticeInquiryService).getNotices(userSeq, page);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("공지사항 상세 조회 테스트")
    void getNoticeDetail_success() {
        // given
        Long noticeSeq = 1L;
        when(noticeInquiryService.getNoticeDetail(noticeSeq)).thenReturn(null);

        // when
        ResponseEntity<NoticeApiResponse> response = noticeController.getNoticeDetail(noticeSeq);

        // then
        verify(noticeInquiryService).getNoticeDetail(noticeSeq);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("안읽은 공지사항 여부 체크 테스트")
    void isNoticeExist_success() {
        // given
        Long userSeq = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(userSeq);
        when(noticeInquiryService.isUnreadNoticeExist(userSeq)).thenReturn(true);

        // when
        ResponseEntity<NoticeApiResponse> response = noticeController.isNoticeExist(userSeq);

        // then
        verify(noticeInquiryService).isUnreadNoticeExist(userSeq);
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
        verify(noticeModifyService).modifyNotice(token, noticeSeq, param.toCommand());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}