package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.user.domain.model.user.ReportData;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ReportValue {
    @Column(name = "reported_count")
    private Long reportedCount;

    @Column(name = "reported_date")
    private Date reportedDate;

    protected ReportValue() {}

    ReportValue(
            Long reportedCount,
            Date reportedDate
    ) {
        this.reportedCount = reportedCount;
        this.reportedDate = reportedDate;
    }

    public static ReportValue of(
            Long reportedCount,
            Date reportedDate
    ) {
        return new ReportValue(reportedCount, reportedDate);
    }

    public ReportData toDomain() {
        return ReportData.of(this.reportedCount, this.reportedDate);
    }
}
