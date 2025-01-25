package com.backend.immilog.user.infrastructure.jpa.repositories;

import com.backend.immilog.user.infrastructure.jpa.entity.report.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportEntity, Long> {

    boolean existsByReportedUserSeqAndReporterUserSeq(
            Long targetUserSeq,
            Long reporterUserSeq
    );
}
