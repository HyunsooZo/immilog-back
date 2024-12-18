package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.user.domain.model.company.Company;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobBoard {
    private final Long seq;
    private final Long userSeq;
    private final PostInfo postInfo;
    private final JobBoardCompany jobBoardCompany;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public JobBoard(
            Long seq,
            Long userSeq,
            PostInfo postInfo,
            JobBoardCompany jobBoardCompany,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.postInfo = postInfo;
        this.jobBoardCompany = jobBoardCompany;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JobBoard of(
            Long userSeq,
            Company company,
            JobBoardUploadCommand command
    ) {
        PostInfo postInfo = PostInfo.of(
                command.title(),
                command.content(),
                company.getCompanyCountry(),
                company.getCompanyRegion()
        );
        JobBoardCompany jobBoardCompany = JobBoardCompany.of(
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
                .postInfo(postInfo)
                .jobBoardCompany(jobBoardCompany)
                .build();
    }

    public void delete() {
        this.postInfo.delete();
    }

}
