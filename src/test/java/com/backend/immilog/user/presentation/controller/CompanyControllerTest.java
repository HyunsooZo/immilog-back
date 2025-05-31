package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.usecase.CompanyFetchUseCase;
import com.backend.immilog.user.application.usecase.CompanyCreateUseCase;
import com.backend.immilog.user.domain.model.company.Industry;
import com.backend.immilog.user.presentation.payload.CompanyPayload;
import com.backend.immilog.user.presentation.payload.UserGeneralResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("CompanyController 테스트")
class CompanyControllerTest {
    private final CompanyCreateUseCase.CompanyCreator companyCreator = mock(CompanyCreateUseCase.CompanyCreator.class);
    private final CompanyFetchUseCase.CompanyFetcher companyFetcher = mock(CompanyFetchUseCase.CompanyFetcher.class);
    private final CompanyController companyController = new CompanyController(
            companyCreator,
            companyFetcher
    );

    @Test
    @DisplayName("회사정보 등록 - 성공")
    void registerCompany() {
        // given
        Long userSeq = 1L;
        String mail = "email@email.com";
        CompanyPayload.CompanyRegisterRequest param = new CompanyPayload.CompanyRegisterRequest(
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
        verify(companyCreator).registerOrEditCompany(anyLong(), any());
    }

    @Test
    @DisplayName("본인 회사정보 조회 - 성공")
    void getCompany() {
        // given
        Long userSeq = 1L;
        when(companyFetcher.getCompany(userSeq)).thenReturn(CompanyResult.createEmpty());
        // when
        ResponseEntity<CompanyPayload.UserCompanyResponse> response = companyController.getCompany(userSeq);
        // then
        verify(companyFetcher).getCompany(anyLong());
    }
}