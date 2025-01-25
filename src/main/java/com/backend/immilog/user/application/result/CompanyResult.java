package com.backend.immilog.user.application.result;

import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
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
                .companyName(company.getName())
                .companyEmail(company.getEmail())
                .companyPhone(company.getPhone())
                .companyAddress(company.getAddress())
                .companyHomepage(company.getHomepage())
                .companyCountry(company.getCountry())
                .companyRegion(company.getRegion())
                .companyLogo(company.getLogo())
                .companyManagerUserSeq(company.getManagerUserSeq())
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