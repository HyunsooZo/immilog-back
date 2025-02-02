package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.post.JobBoardCompany;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class JobBoardCompanyValue {
    @Column(name = "company_seq")
    private Long companySeq;

    @Column(name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @Column(name = "experience")
    @Enumerated(EnumType.STRING)
    private Experience experience;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "salary")
    private String salary;

    @Column(name = "company")
    private String company;

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

    protected JobBoardCompanyValue() {}

    JobBoardCompanyValue(
            Long companySeq,
            Industry industry,
            Experience experience,
            LocalDateTime deadline,
            String salary,
            String company,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo
    ) {
        this.companySeq = companySeq;
        this.industry = industry;
        this.experience = experience;
        this.deadline = deadline;
        this.salary = salary;
        this.company = company;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyLogo = companyLogo;
    }

    public static JobBoardCompanyValue of(
            Long companySeq,
            Industry industry,
            Experience experience,
            LocalDateTime deadline,
            String salary,
            String company,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo
    ) {
        return new JobBoardCompanyValue(
                companySeq,
                industry,
                experience,
                deadline,
                salary,
                company,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo
        );
    }

    public JobBoardCompany toDomain() {
        return new JobBoardCompany(
                this.companySeq,
                this.industry,
                this.experience,
                this.deadline,
                this.salary,
                this.company,
                this.companyEmail,
                this.companyPhone,
                this.companyAddress,
                this.companyHomepage,
                this.companyLogo
        );
    }
}
