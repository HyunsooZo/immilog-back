package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.user.domain.model.user.ReportData;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ReportJpaValue {
    @Column(name = "reported_count")
    private Long reportedCount;

    @Column(name = "reported_date")
    private Date reportedDate;

    protected ReportJpaValue() {}

    ReportJpaValue(
            Long reportedCount,
            Date reportedDate
    ) {
        this.reportedCount = reportedCount;
        this.reportedDate = reportedDate;
    }

    public static ReportJpaValue of(
            Long reportedCount,
            Date reportedDate
    ) {
        return new ReportJpaValue(reportedCount, reportedDate);
    }

    public ReportData toDomain() {
        return ReportData.of(this.reportedCount, this.reportedDate);
    }
}
