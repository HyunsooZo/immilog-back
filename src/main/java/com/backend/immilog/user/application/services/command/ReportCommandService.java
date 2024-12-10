package com.backend.immilog.user.application.services.command;

import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportCommandService {
    private final ReportRepository reportRepository;

    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);
    }
}
