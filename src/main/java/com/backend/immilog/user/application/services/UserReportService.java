package com.backend.immilog.user.application.services;

import com.backend.immilog.global.aop.lock.DistributedLock;
import com.backend.immilog.user.application.command.UserReportCommand;
import com.backend.immilog.user.application.services.command.ReportCommandService;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.ReportQueryService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.domain.enums.ReportReason.OTHER;
import static com.backend.immilog.user.exception.UserErrorCode.*;

@Slf4j
@Service
public class UserReportService {
    final String LOCK_KEY = "reportUser : ";
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    public UserReportService(
            UserQueryService userQueryService,
            UserCommandService userCommandService,
            ReportCommandService reportCommandService,
            ReportQueryService reportQueryService
    ) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.reportCommandService = reportCommandService;
        this.reportQueryService = reportQueryService;
    }

    @Async
    @DistributedLock(key = LOCK_KEY, identifier = "#targetUserSeq", expireTime = 5)
    public void reportUser(
            Long targetUserSeq,
            Long reporterUserSeq,
            UserReportCommand userReportCommand
    ) {
        validateDifferentUsers(targetUserSeq, reporterUserSeq);
        validateItsNotDuplicatedReport(targetUserSeq, reporterUserSeq);
        User user = getUser(targetUserSeq);
        user.increaseReportedCount();
        Report report = Report.of(
                targetUserSeq,
                reporterUserSeq,
                userReportCommand,
                userReportCommand.reason().equals(OTHER)
        );
        reportCommandService.save(report);
        userCommandService.save(user);
        log.info("User {} reported by {}", targetUserSeq, reporterUserSeq);
    }

    private void validateItsNotDuplicatedReport(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        if (targetUserSeq.equals(reporterUserSeq)) {
            throw new UserException(CANNOT_REPORT_MYSELF);
        }
    }

    private void validateDifferentUsers(
            Long targetUserSeq,
            Long reporterUserSeq
    ) {
        boolean isExist = reportQueryService.existsByUserSeqNumbers(
                targetUserSeq,
                reporterUserSeq
        );
        if (isExist) {
            throw new UserException(ALREADY_REPORTED);
        }
    }

    private User getUser(Long targetUserSeq) {
        return userQueryService
                .getUserById(targetUserSeq)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
