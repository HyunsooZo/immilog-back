package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.model.BaseDateEntity;
import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
public class ReportEntity extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long reportedUserSeq;
    private Long reporterUserSeq;
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    protected ReportEntity() {}

    @Builder
    protected ReportEntity(
            Long seq,
            Long reportedUserSeq,
            Long reporterUserSeq,
            String description,
            ReportReason reason
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
