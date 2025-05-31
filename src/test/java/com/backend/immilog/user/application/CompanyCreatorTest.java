package com.backend.immilog.user.application;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.CompanyMapper;
import com.backend.immilog.user.application.usecase.CompanyCreateUseCase;
import com.backend.immilog.user.application.services.CompanyCommandService;
import com.backend.immilog.user.application.services.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CompanyRegisterService 테스트")
class CompanyCreatorTest {
    private final CompanyQueryService companyQueryService = mock(CompanyQueryService.class);
    private final CompanyCommandService companyCommandService = mock(CompanyCommandService.class);
    private final CompanyMapper companyMapper = new CompanyMapper();
    private final CompanyCreateUseCase.CompanyCreator companyCreator = new CompanyCreateUseCase.CompanyCreator(
            companyCommandService,
            companyQueryService,
            companyMapper
    );

    @Test
    @DisplayName("회사정보 등록 - 성공 : 신규")
    void registerCompany() {
        // given
        Long userSeq = 1L;
        var command = new CompanyRegisterCommand(
                Industry.IT,
                "회사명",
                "email@email.com",
                "010-1234-5678",
                "주소",
                "홈페이지",
                Country.SOUTH_KOREA,
                "지역",
                "로고"
        );
        when(companyQueryService.getByCompanyManagerUserSeq(userSeq)).thenReturn(Company.createEmpty());
        // when
        companyCreator.registerOrEditCompany(userSeq, command);
        // then
        verify(companyCommandService).save(any());
    }

    @Test
    @DisplayName("회사정보 등록 - 성공 : 업데이트")
    void registerCompany_update() {
        // given
        Long userSeq = 1L;
        CompanyRegisterCommand command = new CompanyRegisterCommand(
                Industry.IT,
                "회사명",
                "email@email.com",
                "010-1234-5678",
                "주소",
                "홈페이지",
                Country.SOUTH_KOREA,
                "지역",
                "로고"
        );
        var company = Company.createEmpty()
                .manager(command.country(), command.region(), userSeq)
                .companyData(command.industry(), command.name(), command.email(), command.phone(), command.address(), command.homepage(), command.logo());
        when(companyQueryService.getByCompanyManagerUserSeq(userSeq)).thenReturn(company);
        // when
        companyCreator.registerOrEditCompany(userSeq, command);
        // then
        verify(companyCommandService).save(any());
    }

}