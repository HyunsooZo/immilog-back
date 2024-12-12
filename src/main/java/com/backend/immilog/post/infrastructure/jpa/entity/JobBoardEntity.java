package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.global.model.BaseDateEntity;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.JobBoardCompany;
import com.backend.immilog.post.domain.model.post.PostInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicUpdate
@Entity
@Table(name = "job_board")
public class JobBoardEntity extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long userSeq;

    @Embedded
    private PostInfo postInfo;

    @Embedded
    private JobBoardCompany jobBoardCompany;

    public static JobBoardEntity from(
            JobBoard jobBoard
    ) {
        return JobBoardEntity.builder()
                .seq(jobBoard.getSeq())
                .userSeq(jobBoard.getUserSeq())
                .postInfo(jobBoard.getPostInfo())
                .jobBoardCompany(jobBoard.getJobBoardCompany())
                .build();
    }

    public JobBoard toDomain() {
        return JobBoard.builder()
                .seq(seq)
                .userSeq(userSeq)
                .postInfo(postInfo)
                .jobBoardCompany(jobBoardCompany)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
