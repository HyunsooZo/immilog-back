package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Manager;
import com.backend.immilog.user.infrastructure.jpa.entity.company.CompanyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CompanyEntity 테스트")
class CompanyEntityTest {
    @Test
    @DisplayName("CompanyEntity Company 컨버팅")
    void companyEntityFromCompany_validCompany() {
        Company company = Company.builder()
                .seq(1L)
                .companyData(CompanyData.of(
                        Industry.IT,
                        "Test Company",
                        "test@company.com",
                        "1234567890",
                        "123 Test St",
                        "www.test.com",
                        "logo.png"
                ))
                .manager(Manager.of(
                        UserCountry.SOUTH_KOREA,
                        "Test Region",
                        1L
                ))
                .build();

        CompanyEntity companyEntity = CompanyEntity.from(company);
        Company domain = companyEntity.toDomain();

        assertThat(domain.getIndustry()).isEqualTo(company.getIndustry());
        assertThat(domain.getName()).isEqualTo(company.getName());
        assertThat(domain.getEmail()).isEqualTo(company.getEmail());
        assertThat(domain.getPhone()).isEqualTo(company.getPhone());
        assertThat(domain.getAddress()).isEqualTo(company.getAddress());
        assertThat(domain.getHomepage()).isEqualTo(company.getHomepage());
        assertThat(domain.getCountry()).isEqualTo(company.getCountry());
        assertThat(domain.getRegion()).isEqualTo(company.getRegion());
        assertThat(domain.getLogo()).isEqualTo(company.getLogo());
        assertThat(domain.getManagerUserSeq()).isEqualTo(company.getManagerUserSeq());
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

        assertThat(company.getCompanySeq()).isEqualTo(1L);
        assertThat(company.getIndustry()).isEqualTo(Industry.IT);
        assertThat(company.getName()).isEqualTo("Test Company");
        assertThat(company.getEmail()).isEqualTo("test@company.com");
        assertThat(company.getPhone()).isEqualTo("1234567890");
        assertThat(company.getAddress()).isEqualTo("123 Test St");
        assertThat(company.getHomepage()).isEqualTo("www.test.com");
        assertThat(company.getCountry()).isEqualTo(UserCountry.SOUTH_KOREA);
        assertThat(company.getRegion()).isEqualTo("Test Region");
        assertThat(company.getLogo()).isEqualTo("logo.png");
        assertThat(company.getManagerUserSeq()).isEqualTo(1L);
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

        assertThat(company.getCompanySeq()).isNull();
        assertThat(company.getIndustry()).isNull();
        assertThat(company.getName()).isNull();
        assertThat(company.getEmail()).isNull();
        assertThat(company.getPhone()).isNull();
        assertThat(company.getAddress()).isNull();
        assertThat(company.getHomepage()).isNull();
        assertThat(company.getCountry()).isNull();
        assertThat(company.getRegion()).isNull();
        assertThat(company.getLogo()).isNull();
        assertThat(company.getManagerUserSeq()).isNull();
    }
}