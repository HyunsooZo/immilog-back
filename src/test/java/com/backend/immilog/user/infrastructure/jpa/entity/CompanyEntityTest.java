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
        Company domain = companyEntity.toDomain();

        assertThat(domain.getIndustry()).isEqualTo(company.getIndustry());
        assertThat(domain.getCompanyName()).isEqualTo(company.getCompanyName());
        assertThat(domain.getCompanyEmail()).isEqualTo(company.getCompanyEmail());
        assertThat(domain.getCompanyPhone()).isEqualTo(company.getCompanyPhone());
        assertThat(domain.getCompanyAddress()).isEqualTo(company.getCompanyAddress());
        assertThat(domain.getCompanyHomepage()).isEqualTo(company.getCompanyHomepage());
        assertThat(domain.getCompanyCountry()).isEqualTo(company.getCompanyCountry());
        assertThat(domain.getCompanyRegion()).isEqualTo(company.getCompanyRegion());
        assertThat(domain.getCompanyLogo()).isEqualTo(company.getCompanyLogo());
        assertThat(domain.getCompanyManagerUserSeq()).isEqualTo(company.getCompanyManagerUserSeq());
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

        assertThat(company.getSeq()).isEqualTo(1L);
        assertThat(company.getIndustry()).isEqualTo(Industry.IT);
        assertThat(company.getCompanyName()).isEqualTo("Test Company");
        assertThat(company.getCompanyEmail()).isEqualTo("test@company.com");
        assertThat(company.getCompanyPhone()).isEqualTo("1234567890");
        assertThat(company.getCompanyAddress()).isEqualTo("123 Test St");
        assertThat(company.getCompanyHomepage()).isEqualTo("www.test.com");
        assertThat(company.getCompanyCountry()).isEqualTo(UserCountry.SOUTH_KOREA);
        assertThat(company.getCompanyRegion()).isEqualTo("Test Region");
        assertThat(company.getCompanyLogo()).isEqualTo("logo.png");
        assertThat(company.getCompanyManagerUserSeq()).isEqualTo(1L);
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