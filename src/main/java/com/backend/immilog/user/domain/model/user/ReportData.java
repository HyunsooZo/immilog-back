package com.backend.immilog.user.domain.model.user;

import java.sql.Date;

public record ReportData(
        Long reportedCount,
        Date reportedDate
) {
    public static ReportData of(
            Long reportedCount,
            Date reportedDate
    ) {
        return new ReportData(reportedCount, reportedDate);
    }
}
