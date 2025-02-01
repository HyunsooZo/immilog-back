package com.backend.immilog.user.infrastructure.jpa.entity.company;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.model.company.Company;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private ManagerValue manager;

    @Embedded
    private CompanyDataValue companyData;

    protected CompanyEntity() {}

    protected CompanyEntity(
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
        CompanyDataValue companyData = CompanyDataValue.of(
                industry,
                companyName,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo
        );
        ManagerValue manager = ManagerValue.of(
                companyCountry,
                companyRegion,
                companyManagerUserSeq
        );
        this.seq = seq;
        this.manager = manager;
        this.companyData = companyData;
    }

    public static CompanyEntity from(Company company) {
        return new CompanyEntity(
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
        return new Company(this.seq, this.manager.toDomain(), this.companyData.toDomain());
    }
}
