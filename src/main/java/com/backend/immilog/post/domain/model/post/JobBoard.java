package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.user.domain.model.company.Company;

import java.time.LocalDateTime;

public record JobBoard(
        Long seq,
        Long userSeq,
        PostInfo postInfo,
        JobBoardCompany jobBoardCompany,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static JobBoard of(
            Long userSeq,
            Company company,
            String title,
            String content,
            Experience experience,
            LocalDateTime deadline,
            String salary
    ) {
        PostInfo postInfo = PostInfo.of(
                title,
                content,
                company.country(),
                company.region()
        );
        JobBoardCompany jobBoardCompany = JobBoardCompany.of(
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
        return new JobBoard(
                null,
                userSeq,
                postInfo,
                jobBoardCompany,
                LocalDateTime.now(),
                null
        );
    }

    public JobBoard delete() {
        return new JobBoard(
                this.seq,
                this.userSeq,
                new PostInfo(
                        this.postInfo.title(),
                        this.postInfo.content(),
                        this.postInfo.viewCount(),
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        PostStatus.DELETED,
                        this.postInfo.country()
                ),
                this.jobBoardCompany,
                this.createdAt,
                LocalDateTime.now()
        );
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
                this.seq,
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
                this.userSeq,
                this.createdAt
        );
    }

    public static JobBoard from(JobBoardResult jobBoardResult) {
        return new JobBoard(
                jobBoardResult.getSeq(),
                jobBoardResult.getCompanyManagerUserSeq(),
                new PostInfo(
                        jobBoardResult.getTitle(),
                        jobBoardResult.getContent(),
                        jobBoardResult.getViewCount(),
                        jobBoardResult.getLikeCount(),
                        jobBoardResult.getRegion(),
                        jobBoardResult.getStatus(),
                        jobBoardResult.getCountry()
                ),
                new JobBoardCompany(
                        jobBoardResult.getCompanySeq(),
                        jobBoardResult.getIndustry(),
                        jobBoardResult.getExperience(),
                        jobBoardResult.getDeadline(),
                        jobBoardResult.getSalary(),
                        jobBoardResult.getCompanyName(),
                        jobBoardResult.getCompanyEmail(),
                        jobBoardResult.getCompanyPhone(),
                        jobBoardResult.getCompanyAddress(),
                        jobBoardResult.getCompanyHomepage(),
                        jobBoardResult.getCompanyLogo()
                ),
                jobBoardResult.getCreatedAt(),
                null
        );
    }
}
