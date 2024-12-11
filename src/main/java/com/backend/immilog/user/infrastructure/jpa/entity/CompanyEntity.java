package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.company.Company;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Enumerated(EnumType.STRING)
    private Industry industry;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyHomepage;
    private UserCountry companyCountry;
    private String companyRegion;
    private String companyLogo;
    private Long companyManagerUserSeq;

    @Builder
    CompanyEntity(
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

    public static CompanyEntity from(
            Company company
    ) {
        return CompanyEntity.builder()
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
                .seq(seq)
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
