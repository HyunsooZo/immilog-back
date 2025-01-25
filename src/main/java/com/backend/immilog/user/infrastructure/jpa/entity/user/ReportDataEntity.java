package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.user.domain.model.user.ReportData;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Date;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ReportDataEntity {
    private Long reportedCount;
    private Date reportedDate;

    protected ReportDataEntity() {}

    ReportDataEntity(
            Long reportedCount,
            Date reportedDate
    ) {
        this.reportedCount = reportedCount;
        this.reportedDate = reportedDate;
    }

    public static ReportDataEntity of(
            Long reportedCount,
            Date reportedDate
    ) {
        return new ReportDataEntity(reportedCount, reportedDate);
    }

    public ReportData toDomain() {
        return ReportData.of(this.reportedCount, this.reportedDate);
    }
}
