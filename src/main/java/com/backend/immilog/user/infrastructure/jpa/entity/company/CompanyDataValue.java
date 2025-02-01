package com.backend.immilog.user.infrastructure.jpa.entity.company;

import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.model.company.CompanyData;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class CompanyDataValue {

    @Enumerated(EnumType.STRING)
    @Column(name = "industry")
    private Industry industry;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_homepage")
    private String companyHomepage;

    @Column(name = "company_logo")
    private String companyLogo;

    protected CompanyDataValue() {}

    protected CompanyDataValue(
            Industry industry,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo
    ) {
        this.industry = industry;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyLogo = companyLogo;
    }

    public static CompanyDataValue of(
            Industry industry,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo
    ) {
        return new CompanyDataValue(
                industry,
                companyName,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo
        );
    }

    public CompanyData toDomain() {
        return new CompanyData(
                this.industry,
                this.companyName,
                this.companyEmail,
                this.companyPhone,
                this.companyAddress,
                this.companyHomepage,
                this.companyLogo
        );
    }
}
