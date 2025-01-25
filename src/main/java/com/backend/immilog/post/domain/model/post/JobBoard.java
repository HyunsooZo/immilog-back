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
    private final PostData postData;
    private final JobBoardCompany jobBoardCompany;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public JobBoard(
            Long seq,
            Long userSeq,
            PostData postData,
            JobBoardCompany jobBoardCompany,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.postData = postData;
        this.jobBoardCompany = jobBoardCompany;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JobBoard of(
            Long userSeq,
            Company company,
            JobBoardUploadCommand command
    ) {
        PostData postData = PostData.of(
                command.title(),
                command.content(),
                company.getCountry(),
                company.getRegion()
        );
        JobBoardCompany jobBoardCompany = JobBoardCompany.of(
                company.getCompanySeq(),
                company.getIndustry().toPostIndustry(),
                command.experience(),
                command.deadline(),
                command.salary(),
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getAddress(),
                company.getHomepage(),
                company.getLogo()
        );
        return JobBoard.builder()
                .userSeq(userSeq)
                .postData(postData)
                .jobBoardCompany(jobBoardCompany)
                .build();
    }

    public void delete() {
        this.postData.delete();
    }

}
