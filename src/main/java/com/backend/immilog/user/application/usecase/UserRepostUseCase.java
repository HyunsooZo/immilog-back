package com.backend.immilog.user.application.usecase;

import com.backend.immilog.global.aop.lock.DistributedLock;
import com.backend.immilog.user.application.command.UserReportCommand;
import com.backend.immilog.user.application.services.ReportCommandService;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.domain.model.report.Report;
import com.backend.immilog.user.domain.model.report.ReportReason;
import com.backend.immilog.user.domain.service.UserReportPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

public interface UserRepostUseCase {
    void reportUser(
            Long targetUserSeq,
            Long reporterUserSeq,
            UserReportCommand command
    );

    @Slf4j
    @Service
    class UserReporter implements UserRepostUseCase {
        private final UserQueryService userQueryService;
        private final UserCommandService userCommandService;
        private final ReportCommandService reportCommandService;
        private final UserReportPolicy userReportPolicy;

        public UserReporter(
                UserQueryService userQueryService,
                UserCommandService userCommandService,
                ReportCommandService reportCommandService,
                UserReportPolicy userReportPolicy
        ) {
            this.userQueryService = userQueryService;
            this.userCommandService = userCommandService;
            this.reportCommandService = reportCommandService;
            this.userReportPolicy = userReportPolicy;
        }

        @Async
        @DistributedLock(key = "'reportUser : '", identifier = "#p0.toString()", expireTime = 5)
        @Override
        public void reportUser(
                Long targetUserSeq,
                Long reporterUserSeq,
                UserReportCommand command
        ) {
            userReportPolicy.validateDifferentUsers(targetUserSeq, reporterUserSeq);
            userReportPolicy.validateDuplicatedRepost(targetUserSeq, reporterUserSeq);
            var user = userQueryService.getUserById(targetUserSeq);
            var updatedUser = user.increaseReportedCount();
            var report = createReport(targetUserSeq, reporterUserSeq, command);
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
            var description = isOther ? command.description() : command.reason().reason();
            return Report.of(targetUserSeq, reporterUserSeq, description, command.reason());
        }
    }
}
