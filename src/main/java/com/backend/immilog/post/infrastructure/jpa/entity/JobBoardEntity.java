package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.post.domain.model.JobBoard;
import com.backend.immilog.post.domain.model.JobBoardCompany;
import com.backend.immilog.post.domain.model.PostInfo;
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
    private PostInfo postInfo;

    @Embedded
    private JobBoardCompany jobBoardCompany;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected JobBoardEntity() {}

    @Builder
    protected JobBoardEntity(
            Long seq,
            Long userSeq,
            PostInfo postInfo,
            JobBoardCompany jobBoardCompany,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.postInfo = postInfo;
        this.jobBoardCompany = jobBoardCompany;
        this.updatedAt = updatedAt;
    }

    public static JobBoardEntity from(JobBoard jobBoard) {
        return JobBoardEntity.builder()
                .seq(jobBoard.getSeq())
                .userSeq(jobBoard.getUserSeq())
                .postInfo(jobBoard.getPostInfo())
                .jobBoardCompany(jobBoard.getJobBoardCompany())
                .updatedAt(jobBoard.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public JobBoard toDomain() {
        return JobBoard.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .postInfo(this.postInfo)
                .jobBoardCompany(this.jobBoardCompany)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
