package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.company.Manager;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ManagerJpaValue {
    @Enumerated(EnumType.STRING)
    @Column(name = "company_country")
    private Country companyCountry;

    @Column(name = "company_region")
    private String companyRegion;

    @Column(name = "company_manager_user_seq")
    private Long companyManagerUserSeq;

    protected ManagerJpaValue() {}

    protected ManagerJpaValue(
            Country companyCountry,
            String companyRegion,
            Long companyManagerUserSeq
    ) {
        this.companyCountry = companyCountry;
        this.companyRegion = companyRegion;
        this.companyManagerUserSeq = companyManagerUserSeq;
    }

    public static ManagerJpaValue of(
            Country companyCountry,
            String companyRegion,
            Long companyManagerUserSeq
    ) {
        return new ManagerJpaValue(
                companyCountry,
                companyRegion,
                companyManagerUserSeq
        );
    }

    public Manager toDomain() {
        return Manager.of(companyCountry, companyRegion, companyManagerUserSeq);
    }
}
