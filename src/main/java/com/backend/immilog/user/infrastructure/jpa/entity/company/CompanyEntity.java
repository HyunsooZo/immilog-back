package com.backend.immilog.user.infrastructure.jpa.entity.company;

import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Manager;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private ManagerEntity manager;

    @Embedded
    private CompanyDataEntity companyData;

    protected CompanyEntity() {}

    @Builder
    protected CompanyEntity(
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
        CompanyDataEntity companyData = CompanyDataEntity.of(
                industry,
                companyName,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo
        );
        ManagerEntity manager = ManagerEntity.of(
                companyCountry,
                companyRegion,
                companyManagerUserSeq
        );
        this.seq = seq;
        this.manager = manager;
        this.companyData = companyData;
    }

    public static CompanyEntity from(Company company) {
        return CompanyEntity.builder()
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
                .seq(seq)
                .companyData(
                        CompanyData.of(
                                companyData.getIndustry(),
                                companyData.getCompanyName(),
                                companyData.getCompanyEmail(),
                                companyData.getCompanyPhone(),
                                companyData.getCompanyAddress(),
                                companyData.getCompanyHomepage(),
                                companyData.getCompanyLogo()
                        )
                )
                .manager(
                        Manager.of(
                                manager.getCompanyCountry(),
                                manager.getCompanyRegion(),
                                manager.getCompanyManagerUserSeq()
                        )
                )
                .build();
    }
}
