package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.PostStatus;
import jakarta.persistence.*;
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

    protected JobBoardEntity(
            Long seq,
            Long userSeq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Country country,
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
        return new JobBoardEntity(
                jobBoard.seq(),
                jobBoard.userSeq(),
                jobBoard.title(),
                jobBoard.content(),
                jobBoard.viewCount(),
                jobBoard.likeCount(),
                jobBoard.region(),
                jobBoard.status(),
                jobBoard.country(),
                jobBoard.companySeq(),
                jobBoard.industry(),
                jobBoard.experience(),
                jobBoard.deadline(),
                jobBoard.salary(),
                jobBoard.company(),
                jobBoard.companyEmail(),
                jobBoard.companyPhone(),
                jobBoard.companyAddress(),
                jobBoard.companyHomepage(),
                jobBoard.companyLogo(),
                jobBoard.updatedAt()
        );
    }

    public JobBoard toDomain() {
        return new JobBoard(
                this.seq,
                this.userSeq,
                this.postInfo.toDomain(),
                this.jobBoardCompany.toDomain(),
                this.createdAt,
                this.updatedAt
        );
    }
}
