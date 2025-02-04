package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.global.enums.Country;
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
        Company company = new Company(
                1L,
                Manager.of(Country.SOUTH_KOREA, "Test Region", 1L),
                CompanyData.of(Industry.IT, "Test Company", "test@country.com", "1234567890", "123 Test St", "www.test.com", "logo.png")
        );

        CompanyEntity companyEntity = CompanyEntity.from(company);
        Company domain = companyEntity.toDomain();

        assertThat(domain.industry()).isEqualTo(company.industry());
        assertThat(domain.name()).isEqualTo(company.name());
        assertThat(domain.email()).isEqualTo(company.email());
        assertThat(domain.phone()).isEqualTo(company.phone());
        assertThat(domain.address()).isEqualTo(company.address());
        assertThat(domain.homepage()).isEqualTo(company.homepage());
        assertThat(domain.country()).isEqualTo(company.country());
        assertThat(domain.region()).isEqualTo(company.region());
        assertThat(domain.logo()).isEqualTo(company.logo());
        assertThat(domain.managerUserSeq()).isEqualTo(company.managerUserSeq());
    }

    @DisplayName("CompanyEntity Company 컨버팅 - null Company object")
    void companyEntityToDomain_validCompanyEntity() {
        String mail = "test@country.com";
        Company model = new Company(
                1L,
                Manager.of(Country.SOUTH_KOREA, "Test Region", 1L),
                CompanyData.of(Industry.IT, "Test Company", mail, "1234567890", "123 Test St", "www.test.com", "logo.png")
        );

        CompanyEntity companyEntity = CompanyEntity.from(model);


        Company company = companyEntity.toDomain();

        assertThat(company.seq()).isEqualTo(1L);
        assertThat(company.industry()).isEqualTo(Industry.IT);
        assertThat(company.name()).isEqualTo("Test Company");
        assertThat(company.email()).isEqualTo("test@country.com");
        assertThat(company.phone()).isEqualTo("1234567890");
        assertThat(company.address()).isEqualTo("123 Test St");
        assertThat(company.homepage()).isEqualTo("www.test.com");
        assertThat(company.country()).isEqualTo(Country.SOUTH_KOREA);
        assertThat(company.region()).isEqualTo("Test Region");
        assertThat(company.logo()).isEqualTo("logo.png");
        assertThat(company.managerUserSeq()).isEqualTo(1L);
    }

    @DisplayName("CompanyEntity from - null Company object")
    void companyEntityFromCompany_nullCompany() {
        Company company = null;

        assertThatThrownBy(() -> CompanyEntity.from(company))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("CompanyEntity toDomain - null fields")
    void companyEntityToDomain_nullFields() {
        Company nullCompany = new Company(null, null, null);
        CompanyEntity companyEntity = CompanyEntity.from(nullCompany);

        Company company = companyEntity.toDomain();

        assertThat(company.seq()).isNull();
        assertThat(company.industry()).isNull();
        assertThat(company.name()).isNull();
        assertThat(company.email()).isNull();
        assertThat(company.phone()).isNull();
        assertThat(company.address()).isNull();
        assertThat(company.homepage()).isNull();
        assertThat(company.country()).isNull();
        assertThat(company.region()).isNull();
        assertThat(company.logo()).isNull();
        assertThat(company.managerUserSeq()).isNull();
    }
}