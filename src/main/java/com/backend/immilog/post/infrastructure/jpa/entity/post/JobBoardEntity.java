package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.post.JobBoard;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@DynamicUpdate
@Entity
@Table(name = "job_board")
public class JobBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Embedded
    private PostInfoValue postInfo;

    @Embedded
    private JobBoardCompanyValue jobBoardCompany;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected JobBoardEntity() {}

    @Builder
    protected JobBoardEntity(
            Long seq,
            Long userSeq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Countries country,
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
            String companyLogo,
            LocalDateTime updatedAt
    ) {

        JobBoardCompanyValue jobCompany = new JobBoardCompanyValue(
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

        PostInfoValue postInfo = new PostInfoValue(
                title,
                content,
                viewCount,
                likeCount,
                region,
                status,
                country
        );
        this.seq = seq;
        this.userSeq = userSeq;
        this.postInfo = postInfo;
        this.jobBoardCompany = jobCompany;
        this.updatedAt = updatedAt;
    }

    public static JobBoardEntity from(JobBoard jobBoard) {
        return JobBoardEntity.builder()
                .seq(jobBoard.getSeq())
                .userSeq(jobBoard.getUserSeq())
                .title(jobBoard.getTitle())
                .content(jobBoard.getContent())
                .viewCount(jobBoard.getViewCount())
                .likeCount(jobBoard.getLikeCount())
                .region(jobBoard.getRegion())
                .status(jobBoard.getStatus())
                .country(jobBoard.getCountry())
                .companySeq(jobBoard.getCompanySeq())
                .industry(jobBoard.getIndustry())
                .experience(jobBoard.getExperience())
                .deadline(jobBoard.getDeadline())
                .salary(jobBoard.getSalary())
                .company(jobBoard.getCompany())
                .companyEmail(jobBoard.getCompanyEmail())
                .companyPhone(jobBoard.getCompanyPhone())
                .companyAddress(jobBoard.getCompanyAddress())
                .companyHomepage(jobBoard.getCompanyHomepage())
                .companyLogo(jobBoard.getCompanyLogo())
                .updatedAt(jobBoard.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public JobBoard toDomain() {
        return JobBoard.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .postInfo(this.postInfo.toDomain())
                .jobBoardCompany(this.jobBoardCompany.toDomain())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
