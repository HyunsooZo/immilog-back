package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.JobBoardCompany;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.enums.Industry;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record JobBoardResult(
        Long seq,
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        List<String> tags,
        List<String> attachments,
        List<Long> likeUsers,
        List<Long> bookmarkUsers,
        Countries country,
        String region,
        Industry industry,
        LocalDateTime deadline,
        Experience experience,
        String salary,
        Long companySeq,
        String companyName,
        String companyEmail,
        String companyPhone,
        String companyAddress,
        String companyHomepage,
        String companyLogo,
        Long companyManagerUserSeq,
        PostStatus status,
        LocalDateTime createdAt
) {
    public JobBoard toDomain() {
        PostInfo postInfo = PostInfo.builder()
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .status(this.status)
                .country(this.country)
                .region(this.region)
                .build();

        JobBoardCompany jobBoardCompany = JobBoardCompany.builder()
                .companySeq(this.companySeq)
                .industry(this.industry)
                .experience(this.experience)
                .deadline(this.deadline)
                .salary(this.salary)
                .company(this.companyName)
                .companyEmail(this.companyEmail)
                .companyPhone(this.companyPhone)
                .companyAddress(this.companyAddress)
                .companyHomepage(this.companyHomepage)
                .companyLogo(this.companyLogo)
                .build();

        return JobBoard.builder()
                .seq(this.seq)
                .userSeq(this.companyManagerUserSeq())
                .jobBoardCompany(jobBoardCompany)
                .postInfo(postInfo)
                .build();
    }
}
