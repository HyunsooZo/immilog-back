package com.backend.immilog.user.domain.model.company;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class Company {
    private final Long seq;
    private final Long companyManagerUserSeq;
    private Industry industry;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyHomepage;
    private UserCountry companyCountry;
    private String companyRegion;
    private String companyLogo;

    @Builder
    private Company(
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
        this.seq = seq;
        this.industry = industry;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyCountry = companyCountry;
        this.companyRegion = companyRegion;
        this.companyLogo = companyLogo;
        this.companyManagerUserSeq = companyManagerUserSeq;
    }

    // 정적 팩토리 메서드
    public static Company of(
            Long userSeq,
            CompanyRegisterCommand request
    ) {
        return new Company(
                null,
                request.industry(),
                request.companyName(),
                request.companyEmail(),
                request.companyPhone(),
                request.companyAddress(),
                request.companyHomepage(),
                request.companyCountry(),
                request.companyRegion(),
                request.companyLogo(),
                userSeq
        );
    }

    public void updateCompanyName(String newCompanyName) {
        Optional.ofNullable(newCompanyName)
                .ifPresent(name -> this.companyName = name);
    }

    public void updateCompanyPhone(String newCompanyPhone) {
        Optional.ofNullable(newCompanyPhone)
                .ifPresent(phone -> this.companyPhone = phone);
    }

    public void updateCompanyLogo(String newCompanyLogo) {
        Optional.ofNullable(newCompanyLogo)
                .ifPresent(logo -> this.companyLogo = logo);
    }

    public void updateCompanyHomepage(String newCompanyHomepage) {
        Optional.ofNullable(newCompanyHomepage)
                .ifPresent(homepage -> this.companyHomepage = homepage);
    }

    public void updateCompanyEmail(String newCompanyEmail) {
        Optional.ofNullable(newCompanyEmail)
                .ifPresent(email -> this.companyEmail = email);
    }

    public void updateCompanyCountry(UserCountry newCompanyCountry) {
        Optional.ofNullable(newCompanyCountry)
                .ifPresent(country -> this.companyCountry = country);
    }

    public void updateCompanyAddress(String newCompanyAddress) {
        Optional.ofNullable(newCompanyAddress)
                .ifPresent(address -> this.companyAddress = address);
    }

    public void updateCompanyRegion(String newCompanyRegion) {
        Optional.ofNullable(newCompanyRegion)
                .ifPresent(region -> this.companyRegion = region);
    }

    public void updateCompanyIndustry(Industry industry) {
        Optional.ofNullable(industry)
                .ifPresent(newIndustry -> this.industry = newIndustry);
    }
}
