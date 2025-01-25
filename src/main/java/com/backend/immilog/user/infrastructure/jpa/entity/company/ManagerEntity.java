package com.backend.immilog.user.infrastructure.jpa.entity.company;

import com.backend.immilog.user.domain.enums.UserCountry;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ManagerEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "company_country")
    private UserCountry companyCountry;

    @Column(name = "company_region")
    private String companyRegion;

    @Column(name = "company_manager_user_seq")
    private Long companyManagerUserSeq;

    protected ManagerEntity() {}

    protected ManagerEntity(
            UserCountry companyCountry,
            String companyRegion,
            Long companyManagerUserSeq
    ) {
        this.companyCountry = companyCountry;
        this.companyRegion = companyRegion;
        this.companyManagerUserSeq = companyManagerUserSeq;
    }

    public static ManagerEntity of(
            UserCountry companyCountry,
            String companyRegion,
            Long companyManagerUserSeq
    ) {
        return new ManagerEntity(
                companyCountry,
                companyRegion,
                companyManagerUserSeq
        );
    }
}
