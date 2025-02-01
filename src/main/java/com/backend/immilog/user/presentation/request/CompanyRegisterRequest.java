package com.backend.immilog.user.presentation.request;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.domain.enums.Industry;
import lombok.Builder;

@Builder
public record CompanyRegisterRequest(
        Industry industry,
        String companyName,
        String companyEmail,
        String companyPhone,
        String companyAddress,
        String companyHomepage,
        Country companyCountry,
        String companyRegion,
        String companyLogo
) {
    public CompanyRegisterCommand toCommand() {
        return CompanyRegisterCommand.builder()
                .industry(industry())
                .name(companyName())
                .email(companyEmail())
                .phone(companyPhone())
                .address(companyAddress())
                .homepage(companyHomepage())
                .country(companyCountry())
                .region(companyRegion())
                .logo(companyLogo())
                .build();
    }
}
