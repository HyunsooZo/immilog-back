package com.backend.immilog.user.domain.model.report;

import com.backend.immilog.user.domain.enums.ReportReason;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Report {
    private final Long seq;
    private final Long reportedUserSeq;
    private final Long reporterUserSeq;
    private final String description;
    private final ReportReason reason;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    private Report(
            Long seq,
            Long reportedUserSeq,
            Long reporterUserSeq,
            String description,
            ReportReason reason,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.reportedUserSeq = reportedUserSeq;
        this.reporterUserSeq = reporterUserSeq;
        this.description = description;
        this.reason = reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Report of(
            Long targetUserSeq,
            Long reporterUserSeq,
            String description,
            ReportReason reason
    ) {
        return new Report(
                null,
                targetUserSeq,
                reporterUserSeq,
                description,
                reason,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
