package com.backend.immilog.user.infrastructure.repositories;

import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.repositories.ReportRepository;
import com.backend.immilog.user.infrastructure.jpa.entity.ReportEntity;
import com.backend.immilog.user.infrastructure.jpa.repositories.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {
    private final ReportJpaRepository reportJpaRepository;

    @Override
    public boolean existsByUserSeqNumbers(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        return reportJpaRepository.existsByReportedUserSeqAndReporterUserSeq(
                targetUserSeq,
                reporterUserSeq
        );
    }

    @Override
    public Report save(
            Report report
    ) {
        return reportJpaRepository.save(ReportEntity.from(report)).toDomain();
    }
}
