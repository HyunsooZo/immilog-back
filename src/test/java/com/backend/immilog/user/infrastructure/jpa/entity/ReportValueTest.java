package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.infrastructure.jpa.entity.report.ReportEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReportEntity 테스트")
class ReportValueTest {
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
        Report domain = reportEntity.toDomain();

        assertThat(domain.getReportedUserSeq()).isEqualTo(report.getReportedUserSeq());
        assertThat(domain.getReporterUserSeq()).isEqualTo(report.getReporterUserSeq());
        assertThat(domain.getDescription()).isEqualTo(report.getDescription());
        assertThat(domain.getReason()).isEqualTo(report.getReason());
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

        assertThat(report.getSeq()).isEqualTo(1L);
        assertThat(report.getReportedUserSeq()).isEqualTo(1L);
        assertThat(report.getReporterUserSeq()).isEqualTo(2L);
        assertThat(report.getDescription()).isEqualTo("Test description");
        assertThat(report.getReason()).isEqualTo(ReportReason.SPAM);
    }

    @Test
    @DisplayName("ReportEntity -> Report - null")
    void reportEntityFromReport_nullReport() {
        Report report = null;

        assertThatThrownBy(() -> ReportEntity.from(null)).isInstanceOf(NullPointerException.class);
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