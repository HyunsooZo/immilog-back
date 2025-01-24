package com.backend.immilog.user.domain.model.user;

import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
public class ReportData {
    private Long reportedCount;
    private Date reportedDate;

    public ReportData(
            Long reportedCount,
            Date reportedDate
    ) {
        this.reportedCount = reportedCount;
        this.reportedDate = reportedDate;
    }

    public static ReportData of(
            Long reportedCount,
            Date reportedDate
    ) {
        return new ReportData(reportedCount, reportedDate);
    }

    protected void increaseReportCount() {
        this.reportedCount++;
    }

    public void updateReportedDate() {
        this.reportedDate = new Date(System.currentTimeMillis());
    }

}
