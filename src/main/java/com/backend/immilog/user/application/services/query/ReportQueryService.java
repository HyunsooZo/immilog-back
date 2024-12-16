package com.backend.immilog.user.application.services.query;

import com.backend.immilog.user.domain.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportQueryService {
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public boolean existsByUserSeqNumbers(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        return reportRepository.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq);
    }
}
