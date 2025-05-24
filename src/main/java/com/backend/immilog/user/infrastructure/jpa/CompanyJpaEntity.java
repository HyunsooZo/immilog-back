package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.Industry;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
public class CompanyJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private ManagerJpaValue manager;

    @Embedded
    private CompanyJpaDataValue companyData;

    protected CompanyJpaEntity() {}

    protected CompanyJpaEntity(
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
        CompanyJpaDataValue companyData = CompanyJpaDataValue.of(
                industry,
                companyName,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo
        );
        ManagerJpaValue manager = ManagerJpaValue.of(
                companyCountry,
                companyRegion,
                companyManagerUserSeq
        );
        this.seq = seq;
        this.manager = manager;
        this.companyData = companyData;
    }

    public static CompanyJpaEntity from(Company company) {
        return new CompanyJpaEntity(
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
