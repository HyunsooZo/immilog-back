package com.backend.immilog.user.application.services;

import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.repositories.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportCommandService {
    private final ReportRepository reportRepository;

    public ReportCommandService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);
    }
}
