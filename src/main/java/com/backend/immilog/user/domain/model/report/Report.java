package com.backend.immilog.user.domain.model.report;

import java.time.LocalDateTime;

public record Report(
        Long seq,
        Long reportedUserSeq,
        Long reporterUserSeq,
        String description,
        ReportReason reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
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
