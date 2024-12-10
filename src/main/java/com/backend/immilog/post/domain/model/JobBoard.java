package com.backend.immilog.post.domain.model;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.domain.model.enums.PostStatus;
import com.backend.immilog.post.domain.vo.CompanyMetaData;
import com.backend.immilog.post.domain.vo.PostMetaData;
import com.backend.immilog.user.domain.model.company.Company;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JobBoard(
        Long seq,
        Long userSeq,
        PostMetaData postMetaData,
        CompanyMetaData companyMetaData,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static JobBoard of(
            Long userSeq,
            Company company,
            JobBoardUploadCommand command
    ) {
        PostMetaData postMetaData = PostMetaData.of(
                command.title(),
                command.content(),
                company.getCompanyCountry(),
                company.getCompanyRegion()
        );
        CompanyMetaData companyMetaData = CompanyMetaData.of(
                company.getSeq(),
                company.getIndustry(),
                command.experience(),
                command.deadline(),
                command.salary(),
                company.getCompanyName(),
                company.getCompanyEmail(),
                company.getCompanyPhone(),
                company.getCompanyAddress(),
                company.getCompanyHomepage(),
                company.getCompanyLogo()
        );
        return JobBoard.builder()
                .userSeq(userSeq)
                .postMetaData(postMetaData)
                .companyMetaData(companyMetaData)
                .build();
    }

    public JobBoard toDeleteDomain() {
        PostMetaData postMetaData = this.postMetaData;
        postMetaData.setStatus(PostStatus.DELETED);
        return JobBoard.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .postMetaData(postMetaData)
                .companyMetaData(this.companyMetaData)
                .build();
    }
}
