package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.user.domain.model.company.Company;

import java.time.LocalDateTime;

public class JobBoard {
    private final Long seq;
    private final Long userSeq;
    private PostInfo postInfo;
    private final JobBoardCompany jobBoardCompany;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
            String title,
            String content,
            Experience experience,
            LocalDateTime deadline,
            String salary
    ) {
        var postInfo = PostInfo.of(
                title,
                content,
                company.country(),
                company.region()
        );
        var jobBoardCompany = JobBoardCompany.of(
                company.seq(),
                company.industry().toPostIndustry(),
                experience,
                deadline,
                salary,
                company.name(),
                company.email(),
                company.phone(),
                company.address(),
                company.homepage(),
                company.logo()
        );
        return new JobBoard(null, userSeq, postInfo, jobBoardCompany, LocalDateTime.now(), null);
    }

    public JobBoard delete() {
        this.postInfo = new PostInfo(
                        this.postInfo.title(),
                        this.postInfo.content(),
                        this.postInfo.viewCount(),
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        PostStatus.DELETED,
                        this.postInfo.country()
        );
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Long companySeq() {return this.jobBoardCompany.companySeq();}

    public Industry industry() {return this.jobBoardCompany.industry();}

    public Experience experience() {return this.jobBoardCompany.experience();}

    public LocalDateTime deadline() {return this.jobBoardCompany.deadline();}

    public String salary() {return this.jobBoardCompany.salary();}

    public String company() {return this.jobBoardCompany.company();}

    public String companyEmail() {return this.jobBoardCompany.companyEmail();}

    public String companyPhone() {return this.jobBoardCompany.companyPhone();}

    public String companyAddress() {return this.jobBoardCompany.companyAddress();}

    public String companyHomepage() {return this.jobBoardCompany.companyHomepage();}

    public String companyLogo() {return this.jobBoardCompany.companyLogo();}

    public String title() {return this.postInfo.title();}

    public String content() {return this.postInfo.content();}

    public Long viewCount() {return this.postInfo.viewCount();}

    public Long likeCount() {return this.postInfo.likeCount();}

    public String region() {return this.postInfo.region();}

    public PostStatus status() {return this.postInfo.status();}

    public Country country() {return this.postInfo.country();}

    public JobBoardResult toResult() {
        return new JobBoardResult(
                this.seq(),
                this.title(),
                this.content(),
                this.viewCount(),
                this.likeCount(),
                this.region(),
                this.status(),
                this.country(),
                this.industry(),
                this.experience(),
                this.deadline(),
                this.salary(),
                this.companySeq(),
                this.company(),
                this.companyEmail(),
                this.companyPhone(),
                this.companyAddress(),
                this.companyHomepage(),
                this.companyLogo(),
                this.userSeq(),
                this.createdAt()
        );
    }

    public static JobBoard from(JobBoardResult jobBoardResult) {
        return new JobBoard(
                jobBoardResult.seq(),
                jobBoardResult.companyManagerUserSeq(),
                new PostInfo(
                        jobBoardResult.title(),
                        jobBoardResult.content(),
                        jobBoardResult.viewCount(),
                        jobBoardResult.likeCount(),
                        jobBoardResult.region(),
                        jobBoardResult.status(),
                        jobBoardResult.country()
                ),
                new JobBoardCompany(
                        jobBoardResult.companySeq(),
                        jobBoardResult.industry(),
                        jobBoardResult.experience(),
                        jobBoardResult.deadline(),
                        jobBoardResult.salary(),
                        jobBoardResult.companyName(),
                        jobBoardResult.companyEmail(),
                        jobBoardResult.companyPhone(),
                        jobBoardResult.companyAddress(),
                        jobBoardResult.companyHomepage(),
                        jobBoardResult.companyLogo()
                ),
                jobBoardResult.createdAt(),
                null
        );
    }

    public Long seq() {return seq;}

    public Long userSeq() {return userSeq;}

    public LocalDateTime createdAt() {return createdAt;}

    public LocalDateTime updatedAt() {return updatedAt;}
}
