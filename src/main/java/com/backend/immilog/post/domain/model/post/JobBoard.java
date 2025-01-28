package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.enums.PostStatus;
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
                .postInfo(postInfo)
                .jobBoardCompany(jobBoardCompany)
                .build();
    }

    public JobBoard delete() {
        return JobBoard.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .postInfo(
                        new PostInfo(
                                this.postInfo.title(),
                                this.postInfo.content(),
                                this.postInfo.viewCount(),
                                this.postInfo.likeCount(),
                                this.postInfo.region(),
                                PostStatus.DELETED,
                                this.postInfo.country()
                        )
                )
                .jobBoardCompany(this.jobBoardCompany)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Long getCompanySeq() {return this.jobBoardCompany.companySeq();}

    public Industry getIndustry() {return this.jobBoardCompany.industry();}

    public Experience getExperience() {return this.jobBoardCompany.experience();}

    public LocalDateTime getDeadline() {return this.jobBoardCompany.deadline();}

    public String getSalary() {return this.jobBoardCompany.salary();}

    public String getCompany() {return this.jobBoardCompany.company();}

    public String getCompanyEmail() {return this.jobBoardCompany.companyEmail();}

    public String getCompanyPhone() {return this.jobBoardCompany.companyPhone();}

    public String getCompanyAddress() {return this.jobBoardCompany.companyAddress();}

    public String getCompanyHomepage() {return this.jobBoardCompany.companyHomepage();}

    public String getCompanyLogo() {return this.jobBoardCompany.companyLogo();}

    public String getTitle() {return this.postInfo.title();}

    public String getContent() {return this.postInfo.content();}

    public Long getViewCount() {return this.postInfo.viewCount();}

    public Long getLikeCount() {return this.postInfo.likeCount();}

    public String getRegion() {return this.postInfo.region();}

    public PostStatus getStatus() {return this.postInfo.status();}

    public Countries getCountry() {return this.postInfo.country();}
}
