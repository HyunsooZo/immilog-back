package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;

import java.time.LocalDateTime;

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
        return new JobBoardCompany(
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
}
