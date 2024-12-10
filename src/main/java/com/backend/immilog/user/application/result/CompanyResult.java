package com.backend.immilog.user.application.result;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
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
        UserCountry companyCountry,
        String companyRegion,
        String companyLogo,
        Long companyManagerUserSeq
) {
    public static CompanyResult from(
            Company company
    ) {
        return CompanyResult.builder()
                .industry(company.getIndustry())
                .companyName(company.getCompanyName())
                .companyEmail(company.getCompanyEmail())
                .companyPhone(company.getCompanyPhone())
                .companyAddress(company.getCompanyAddress())
                .companyHomepage(company.getCompanyHomepage())
                .companyCountry(company.getCompanyCountry())
                .companyRegion(company.getCompanyRegion())
                .companyLogo(company.getCompanyLogo())
                .companyManagerUserSeq(company.getCompanyManagerUserSeq())
                .build();
    }

    public Company toDomain() {
        return Company.builder()
                .industry(industry)
                .companyName(companyName)
                .companyEmail(companyEmail)
                .companyPhone(companyPhone)
                .companyAddress(companyAddress)
                .companyHomepage(companyHomepage)
                .companyCountry(companyCountry)
                .companyRegion(companyRegion)
                .companyLogo(companyLogo)
                .companyManagerUserSeq(companyManagerUserSeq)
                .build();
    }
}