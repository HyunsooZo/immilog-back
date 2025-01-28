package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JobBoardCompany(
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
