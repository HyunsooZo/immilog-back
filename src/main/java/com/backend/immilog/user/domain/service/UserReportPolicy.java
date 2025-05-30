package com.backend.immilog.user.domain.service;

import com.backend.immilog.user.application.services.ReportQueryService;
import com.backend.immilog.user.exception.UserException;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.exception.UserErrorCode.ALREADY_REPORTED;
import static com.backend.immilog.user.exception.UserErrorCode.CANNOT_REPORT_MYSELF;

@Service
public class UserReportPolicy {
    private final ReportQueryService reportQueryService;

    public UserReportPolicy(ReportQueryService reportQueryService) {
        this.reportQueryService = reportQueryService;
    }

    public void validateDuplicatedRepost(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        if (targetUserSeq.equals(reporterUserSeq)) {
            throw new UserException(CANNOT_REPORT_MYSELF);
        }
    }

    public void validateDifferentUsers(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        if (reportQueryService.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq)) {
            throw new UserException(ALREADY_REPORTED);
        }
    }
}
