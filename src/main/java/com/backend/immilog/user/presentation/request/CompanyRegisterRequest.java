package com.backend.immilog.user.presentation.request;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.domain.enums.Industry;

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
        return new CompanyRegisterCommand(
                this.industry,
                this.companyName,
                this.companyEmail,
                this.companyPhone,
                this.companyAddress,
                this.companyHomepage,
                this.companyCountry,
                this.companyRegion,
                this.companyLogo
        );
    }
}
