package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Manager;

public record CompanyResult(
        Long seq,
        Industry industry,
        String companyName,
        String companyEmail,
        String companyPhone,
        String companyAddress,
        String companyHomepage,
        Country companyCountry,
        String companyRegion,
        String companyLogo,
        Long companyManagerUserSeq
) {
    public static CompanyResult from(Company company) {
        return new CompanyResult(
                company.seq(),
                company.industry(),
                company.name(),
                company.email(),
                company.phone(),
                company.address(),
                company.homepage(),
                company.country(),
                company.region(),
                company.logo(),
                company.managerUserSeq()
        );
    }

    public Company toDomain() {
        return new Company(
                seq,
                Manager.of(
                        companyCountry,
                        companyRegion,
                        companyManagerUserSeq
                ),
                CompanyData.of(
                        industry,
                        companyName,
                        companyEmail,
                        companyPhone,
                        companyAddress,
                        companyHomepage,
                        companyLogo
                )
        );
    }

    public static CompanyResult empty() {
        return new CompanyResult(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}