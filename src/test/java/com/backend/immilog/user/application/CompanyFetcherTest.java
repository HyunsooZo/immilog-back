package com.backend.immilog.user.application;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.usecase.CompanyFetchUseCase;
import com.backend.immilog.user.application.services.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Manager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CompanyInquiryService 테스트")
class CompanyFetcherTest {
    private final CompanyQueryService companyQueryService = mock(CompanyQueryService.class);
    private final CompanyFetchUseCase.CompanyFetcher companyFetcher = new CompanyFetchUseCase.CompanyFetcher(companyQueryService);

    @Test
    @DisplayName("회사정보 조회 - 성공")
    void getCompany() {
        // given
        Long userSeq = 1L;
        Company company = new Company(
                1L,
                Manager.of(
                        Country.SOUTH_KOREA,
                        "지역",
                        1L
                ),
                CompanyData.of(
                        Industry.IT,
                        "회사명",
                        "이메일",
                        "전화번호",
                        "주소",
                        "홈페이지",
                        "로고"
                )
        );
        when(companyQueryService.getByCompanyManagerUserSeq(userSeq)).thenReturn(company);
        // when
        CompanyResult result = companyFetcher.getCompany(userSeq);
        // then
        Assertions.assertThat(result.companyLogo()).isEqualTo("로고");
    }


}