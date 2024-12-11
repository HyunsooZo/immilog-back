package com.backend.immilog.user.domain.model.user;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReportInfo {
    private Long reportedCount;
    private Date reportedDate;

    @Builder
    ReportInfo(
            Long reportedCount,
            Date reportedDate
    ) {
        this.reportedCount = reportedCount;
        this.reportedDate = reportedDate;
    }

    public static ReportInfo of(
            Long reportedCount,
            Date reportedDate
    ) {
        return ReportInfo.builder()
                .reportedCount(reportedCount)
                .reportedDate(reportedDate)
                .build();
    }
}
