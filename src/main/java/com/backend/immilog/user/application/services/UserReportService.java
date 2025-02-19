package com.backend.immilog.user.application.services;

import com.backend.immilog.global.aop.lock.DistributedLock;
import com.backend.immilog.user.application.command.UserReportCommand;
import com.backend.immilog.user.application.services.command.ReportCommandService;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.ReportQueryService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.exception.UserErrorCode.ALREADY_REPORTED;
import static com.backend.immilog.user.exception.UserErrorCode.CANNOT_REPORT_MYSELF;

@Slf4j
@Service
public class UserReportService {
    final String LOCK_KEY = "'reportUser : '";
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
    @DistributedLock(key = LOCK_KEY, identifier = "#p0.toString()", expireTime = 5)
    public void reportUser(
            Long targetUserSeq,
            Long reporterUserSeq,
            UserReportCommand command
    ) {
        validateDifferentUsers(targetUserSeq, reporterUserSeq);
        validateDuplicatedRepost(targetUserSeq, reporterUserSeq);
        User user = userQueryService.getUserById(targetUserSeq);
        User updatedUser = user.increaseReportedCount();
        Report report = createReport(targetUserSeq, reporterUserSeq, command);
        reportCommandService.save(report);
        userCommandService.save(updatedUser);
        log.info("User {} reported by {}", targetUserSeq, reporterUserSeq);
    }

    private static Report createReport(
            Long targetUserSeq,
            Long reporterUserSeq,
            UserReportCommand command
    ) {
        boolean isOther = command.reason().equals(ReportReason.OTHER);
        String description = isOther ? command.description() : command.reason().reason();
        return Report.of(targetUserSeq, reporterUserSeq, description, command.reason());
    }

    private void validateDuplicatedRepost(
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
        if (reportQueryService.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq)) {
            throw new UserException(ALREADY_REPORTED);
        }
    }
}
