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
        //public record Report(
        //        Long seq,
        //        Long reportedUserSeq,
        //        Long reporterUserSeq,
        //        String description,
        //        ReportReason reason,
        //        LocalDateTime createdAt,
        //        LocalDateTime updatedAt
        //) {
        Report report = new Report(
                1L,
                1L,
                2L,
                "Test description",
                ReportReason.SPAM,
                null,
                null
        );
        ReportEntity reportEntity = ReportEntity.from(report);
        Report domain = reportEntity.toDomain();

        assertThat(domain.reportedUserSeq()).isEqualTo(report.reportedUserSeq());
        assertThat(domain.reporterUserSeq()).isEqualTo(report.reporterUserSeq());
        assertThat(domain.description()).isEqualTo(report.description());
        assertThat(domain.reason()).isEqualTo(report.reason());
    }

    @Test
    @DisplayName("ReportEntity -> toDomain")
    void reportEntityToDomain_validReportEntity() {
        Report model = new Report(
                1L,
                1L,
                2L,
                "Test description",
                ReportReason.SPAM,
                null,
                null
        );
        ReportEntity reportEntity = ReportEntity.from(model);

        Report report = reportEntity.toDomain();

        assertThat(report.seq()).isEqualTo(1L);
        assertThat(report.reportedUserSeq()).isEqualTo(1L);
        assertThat(report.reporterUserSeq()).isEqualTo(2L);
        assertThat(report.description()).isEqualTo("Test description");
        assertThat(report.reason()).isEqualTo(ReportReason.SPAM);
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
        Report nullModel = new Report(null, null, null, null, null, null, null);
        ReportEntity reportEntity = ReportEntity.from(nullModel);

        Report report = reportEntity.toDomain();

        assertThat(report.seq()).isNull();
        assertThat(report.reportedUserSeq()).isNull();
        assertThat(report.reporterUserSeq()).isNull();
        assertThat(report.description()).isNull();
        assertThat(report.reason()).isNull();
    }
}