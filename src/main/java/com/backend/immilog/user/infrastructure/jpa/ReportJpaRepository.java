package com.backend.immilog.user.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportJpaEntity, Long> {

    boolean existsByReportedUserSeqAndReporterUserSeq(
            Long targetUserSeq,
            Long reporterUserSeq
    );
}
