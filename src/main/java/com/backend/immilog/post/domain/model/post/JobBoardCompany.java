package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.user.domain.enums.Industry;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class JobBoardCompany {
    private Long companySeq;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    @Enumerated(EnumType.STRING)
    private Experience experience;

    private LocalDateTime deadline;
    private String salary;
    private String company;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyHomepage;
    private String companyLogo;

    protected JobBoardCompany() {}

    @Builder
    JobBoardCompany(
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

    public static JobBoardCompany of(
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
        return JobBoardCompany.builder()
                .companySeq(companySeq)
                .industry(industry)
                .experience(experience)
                .deadline(deadline)
                .salary(salary)
                .company(company)
                .companyEmail(companyEmail)
                .companyPhone(companyPhone)
                .companyAddress(companyAddress)
                .companyHomepage(companyHomepage)
                .companyLogo(companyLogo)
                .build();
    }
}
