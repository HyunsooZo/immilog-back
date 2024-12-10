package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReportEntity 테스트")
class ReportEntityTest {
    @Test
    @DisplayName("ReportEntity -> Report")
    void reportEntityFromReport_validReport() {
        Report report = Report.builder()
                .reportedUserSeq(1L)
                .reporterUserSeq(2L)
                .description("Test description")
                .reason(ReportReason.SPAM)
                .build();

        ReportEntity reportEntity = ReportEntity.from(report);

        assertThat(reportEntity.getReportedUserSeq()).isEqualTo(report.getReportedUserSeq());
        assertThat(reportEntity.getReporterUserSeq()).isEqualTo(report.getReporterUserSeq());
        assertThat(reportEntity.getDescription()).isEqualTo(report.getDescription());
        assertThat(reportEntity.getReason()).isEqualTo(report.getReason());
    }

    @Test
    @DisplayName("ReportEntity -> toDomain")
    void reportEntityToDomain_validReportEntity() {
        ReportEntity reportEntity = ReportEntity.builder()
                .seq(1L)
                .reportedUserSeq(1L)
                .reporterUserSeq(2L)
                .description("Test description")
                .reason(ReportReason.SPAM)
                .build();

        Report report = reportEntity.toDomain();

        assertThat(report.getSeq()).isEqualTo(reportEntity.getSeq());
        assertThat(report.getReportedUserSeq()).isEqualTo(reportEntity.getReportedUserSeq());
        assertThat(report.getReporterUserSeq()).isEqualTo(reportEntity.getReporterUserSeq());
        assertThat(report.getDescription()).isEqualTo(reportEntity.getDescription());
        assertThat(report.getReason()).isEqualTo(reportEntity.getReason());
    }

    @Test
    @DisplayName("ReportEntity -> Report - null")
    void reportEntityFromReport_nullReport() {
        Report report = null;

        assertThatThrownBy(() -> ReportEntity.from(report))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("ReportEntity -> toDomain")
    void reportEntityToDomain_nullFields() {
        ReportEntity reportEntity = ReportEntity.builder().build();

        Report report = reportEntity.toDomain();

        assertThat(report.getSeq()).isNull();
        assertThat(report.getReportedUserSeq()).isNull();
        assertThat(report.getReporterUserSeq()).isNull();
        assertThat(report.getDescription()).isNull();
        assertThat(report.getReason()).isNull();
    }
}