package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.CompanyInquiryService;
import com.backend.immilog.user.application.services.CompanyRegisterService;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.presentation.request.CompanyRegisterRequest;
import com.backend.immilog.user.presentation.response.UserCompanyResponse;
import com.backend.immilog.user.presentation.response.UserGeneralResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

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
        Long userSeq = 1L;
        String mail = "email@email.com";
        CompanyRegisterRequest param = new CompanyRegisterRequest(
                Industry.IT,
                "회사명",
                mail,
                "010-1234-5678",
                "주소",
                "홈페이지",
                Country.SOUTH_KOREA,
                "지역",
                "로고"
        );
        CompanyRegisterCommand command = param.toCommand();
        // when
        ResponseEntity<UserGeneralResponse> response = companyController.registerCompany(userSeq, param);
        // then
        verify(companyRegisterService).registerOrEditCompany(anyLong(), any());
    }

    @Test
    @DisplayName("본인 회사정보 조회 - 성공")
    void getCompany() {
        // given
        Long userSeq = 1L;
        when(companyInquiryService.getCompany(userSeq)).thenReturn(CompanyResult.empty());
        // when
        ResponseEntity<UserCompanyResponse> response = companyController.getCompany(userSeq);
        // then
        verify(companyInquiryService).getCompany(anyLong());
    }
}