package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CompanyEntity 테스트")
class CompanyEntityTest {
    @Test
    @DisplayName("CompanyEntity Company 컨버팅")
    void companyEntityFromCompany_validCompany() {
        Company company = Company.builder()
                .industry(Industry.IT)
                .companyName("Test Company")
                .companyEmail("test@company.com")
                .companyPhone("1234567890")
                .companyAddress("123 Test St")
                .companyHomepage("www.test.com")
                .companyCountry(UserCountry.SOUTH_KOREA)
                .companyRegion("Test Region")
                .companyLogo("logo.png")
                .companyManagerUserSeq(1L)
                .build();

        CompanyEntity companyEntity = CompanyEntity.from(company);

        assertThat(companyEntity.getIndustry()).isEqualTo(company.getIndustry());
        assertThat(companyEntity.getCompanyName()).isEqualTo(company.getCompanyName());
        assertThat(companyEntity.getCompanyEmail()).isEqualTo(company.getCompanyEmail());
        assertThat(companyEntity.getCompanyPhone()).isEqualTo(company.getCompanyPhone());
        assertThat(companyEntity.getCompanyAddress()).isEqualTo(company.getCompanyAddress());
        assertThat(companyEntity.getCompanyHomepage()).isEqualTo(company.getCompanyHomepage());
        assertThat(companyEntity.getCompanyCountry()).isEqualTo(company.getCompanyCountry());
        assertThat(companyEntity.getCompanyRegion()).isEqualTo(company.getCompanyRegion());
        assertThat(companyEntity.getCompanyLogo()).isEqualTo(company.getCompanyLogo());
        assertThat(companyEntity.getCompanyManagerUserSeq()).isEqualTo(company.getCompanyManagerUserSeq());
    }

    @DisplayName("CompanyEntity Company 컨버팅 - null Company object")
    void companyEntityToDomain_validCompanyEntity() {
        CompanyEntity companyEntity = CompanyEntity.builder()
                .seq(1L)
                .industry(Industry.IT)
                .companyName("Test Company")
                .companyEmail("test@company.com")
                .companyPhone("1234567890")
                .companyAddress("123 Test St")
                .companyHomepage("www.test.com")
                .companyCountry(UserCountry.SOUTH_KOREA)
                .companyRegion("Test Region")
                .companyLogo("logo.png")
                .companyManagerUserSeq(1L)
                .build();

        Company company = companyEntity.toDomain();

        assertThat(company.getSeq()).isEqualTo(companyEntity.getSeq());
        assertThat(company.getIndustry()).isEqualTo(companyEntity.getIndustry());
        assertThat(company.getCompanyName()).isEqualTo(companyEntity.getCompanyName());
        assertThat(company.getCompanyEmail()).isEqualTo(companyEntity.getCompanyEmail());
        assertThat(company.getCompanyPhone()).isEqualTo(companyEntity.getCompanyPhone());
        assertThat(company.getCompanyAddress()).isEqualTo(companyEntity.getCompanyAddress());
        assertThat(company.getCompanyHomepage()).isEqualTo(companyEntity.getCompanyHomepage());
        assertThat(company.getCompanyCountry()).isEqualTo(companyEntity.getCompanyCountry());
        assertThat(company.getCompanyRegion()).isEqualTo(companyEntity.getCompanyRegion());
        assertThat(company.getCompanyLogo()).isEqualTo(companyEntity.getCompanyLogo());
        assertThat(company.getCompanyManagerUserSeq()).isEqualTo(companyEntity.getCompanyManagerUserSeq());
    }

    @DisplayName("CompanyEntity from - null Company object")
    void companyEntityFromCompany_nullCompany() {
        Company company = null;

        assertThatThrownBy(() -> CompanyEntity.from(company))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("CompanyEntity toDomain - null fields")
    void companyEntityToDomain_nullFields() {
        CompanyEntity companyEntity = CompanyEntity.builder().build();

        Company company = companyEntity.toDomain();

        assertThat(company.getSeq()).isNull();
        assertThat(company.getIndustry()).isNull();
        assertThat(company.getCompanyName()).isNull();
        assertThat(company.getCompanyEmail()).isNull();
        assertThat(company.getCompanyPhone()).isNull();
        assertThat(company.getCompanyAddress()).isNull();
        assertThat(company.getCompanyHomepage()).isNull();
        assertThat(company.getCompanyCountry()).isNull();
        assertThat(company.getCompanyRegion()).isNull();
        assertThat(company.getCompanyLogo()).isNull();
        assertThat(company.getCompanyManagerUserSeq()).isNull();
    }
}