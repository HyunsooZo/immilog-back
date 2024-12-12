package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.services.CompanyInquiryService;
import com.backend.immilog.user.application.services.CompanyRegisterService;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.presentation.request.CompanyRegisterRequest;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import static com.backend.immilog.user.domain.enums.UserCountry.SOUTH_KOREA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("CompanyController 테스트")
class CompanyControllerTest {
    private final CompanyRegisterService companyRegisterService = mock(CompanyRegisterService.class);
    private final CompanyInquiryService companyInquiryService = mock(CompanyInquiryService.class);
    private final CompanyController companyController = new CompanyController(
            companyRegisterService,
            companyInquiryService
    );

    @Test
    @DisplayName("회사정보 등록 - 성공")
    void registerCompany() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        CompanyRegisterRequest param = CompanyRegisterRequest.builder()
                .industry(Industry.IT)
                .companyName("회사명")
                .companyEmail("email@email.com")
                .companyPhone("010-1234-5678")
                .companyAddress("주소")
                .companyHomepage("홈페이지")
                .companyCountry(SOUTH_KOREA)
                .companyRegion("지역")
                .companyLogo("로고")
                .build();
        CompanyRegisterCommand command = param.toCommand();
        when(request.getAttribute("userSeq")).thenReturn(1L);
        // when
        ResponseEntity<UserApiResponse> response = companyController.registerCompany(request, param);
        // then
        verify(companyRegisterService).registerOrEditCompany(anyLong(), any());
    }

    @Test
    @DisplayName("본인 회사정보 조회 - 성공")
    void getCompany() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(1L);
        // when
        ResponseEntity<UserApiResponse> response = companyController.getCompany(request);
        // then
        verify(companyInquiryService).getCompany(anyLong());
    }
}