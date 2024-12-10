package com.backend.immilog.user.application;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.CompanyRegisterService;
import com.backend.immilog.user.application.services.command.CompanyCommandService;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.backend.immilog.user.domain.enums.UserCountry.SOUTH_KOREA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CompanyRegisterService 테스트")
class CompanyRegisterServiceTest {
    private final CompanyQueryService companyQueryService = mock(CompanyQueryService.class);
    private final CompanyCommandService companyCommandService = mock(CompanyCommandService.class);
    private final CompanyRegisterService companyRegisterService = new CompanyRegisterService(
            companyQueryService,
            companyCommandService
    );

    @Test
    @DisplayName("회사정보 등록 - 성공 : 신규")
    void registerCompany() {
        // given
        Long userSeq = 1L;
        CompanyRegisterCommand command = CompanyRegisterCommand.builder()
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
        // when
        companyRegisterService.registerOrEditCompany(userSeq, command);
        // then
        verify(companyCommandService).save(any());
    }

    @Test
    @DisplayName("회사정보 등록 - 성공 : 업데이트")
    void registerCompany_update() {
        // given
        Long userSeq = 1L;
        CompanyRegisterCommand command = CompanyRegisterCommand.builder()
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
        Company company = Company.of(userSeq, command);
        when(companyQueryService.getByCompanyManagerUserSeq(userSeq)).thenReturn(Optional.of(company));
        // when
        companyRegisterService.registerOrEditCompany(userSeq, command);
        // then
        verify(companyCommandService).save(any());
    }

}