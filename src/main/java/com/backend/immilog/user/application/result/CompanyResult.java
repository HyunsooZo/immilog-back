package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Manager;
import lombok.Builder;

@Builder
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
    public static CompanyResult from(
            Company company
    ) {
        return CompanyResult.builder()
                .industry(company.industry())
                .companyName(company.name())
                .companyEmail(company.email())
                .companyPhone(company.phone())
                .companyAddress(company.address())
                .companyHomepage(company.homepage())
                .companyCountry(company.country())
                .companyRegion(company.region())
                .companyLogo(company.logo())
                .companyManagerUserSeq(company.managerUserSeq())
                .build();
    }

    public Company toDomain() {
        return Company.builder()
                .manager(
                        Manager.of(
                                companyCountry,
                                companyRegion,
                                companyManagerUserSeq
                        )
                )
                .companyData(
                        CompanyData.of(
                                industry,
                                companyName,
                                companyEmail,
                                companyPhone,
                                companyAddress,
                                companyHomepage,
                                companyLogo
                        )
                )
                .build();
    }
}