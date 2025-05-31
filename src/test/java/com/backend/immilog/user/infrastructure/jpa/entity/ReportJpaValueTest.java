package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.user.domain.model.report.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.infrastructure.jpa.ReportJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReportEntity 테스트")
class ReportJpaValueTest {
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
        ReportJpaEntity reportJpaEntity = ReportJpaEntity.from(report);
        Report domain = reportJpaEntity.toDomain();

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
        ReportJpaEntity reportJpaEntity = ReportJpaEntity.from(model);

        Report report = reportJpaEntity.toDomain();

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

        assertThatThrownBy(() -> ReportJpaEntity.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("ReportEntity -> toDomain")
    void reportEntityToDomain_nullFields() {
        Report nullModel = new Report(null, null, null, null, null, null, null);
        ReportJpaEntity reportJpaEntity = ReportJpaEntity.from(nullModel);

        Report report = reportJpaEntity.toDomain();

        assertThat(report.seq()).isNull();
        assertThat(report.reportedUserSeq()).isNull();
        assertThat(report.reporterUserSeq()).isNull();
        assertThat(report.description()).isNull();
        assertThat(report.reason()).isNull();
    }
}