package com.backend.immilog.user.domain.model.user;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Embeddable
public class ReportInfo {
    private Long reportedCount;
    private Date reportedDate;

    protected ReportInfo() {}

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
