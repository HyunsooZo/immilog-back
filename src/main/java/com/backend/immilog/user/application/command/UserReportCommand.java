package com.backend.immilog.user.application.command;

import com.backend.immilog.user.domain.enums.ReportReason;

public record UserReportCommand(
        ReportReason reason,
        String description
) {
}
