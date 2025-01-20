package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@DynamicUpdate
@Entity
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "reported_user_seq")
    private Long reportedUserSeq;

    @Column(name = "reporter_user_seq")
    private Long reporterUserSeq;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private ReportReason reason;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected ReportEntity() {}

    @Builder
    protected ReportEntity(
            Long seq,
            Long reportedUserSeq,
            Long reporterUserSeq,
            String description,
            ReportReason reason,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.reportedUserSeq = reportedUserSeq;
        this.reporterUserSeq = reporterUserSeq;
        this.description = description;
        this.reason = reason;
    }

    public static ReportEntity from(Report report) {
        return ReportEntity.builder()
                .reportedUserSeq(report.getReportedUserSeq())
                .reporterUserSeq(report.getReporterUserSeq())
                .description(report.getDescription())
                .reason(report.getReason())
                .updatedAt(report.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public Report toDomain() {
        return Report.builder()
                .seq(seq)
                .reportedUserSeq(reportedUserSeq)
                .reporterUserSeq(reporterUserSeq)
                .description(description)
                .reason(reason)
                .build();
    }
}
