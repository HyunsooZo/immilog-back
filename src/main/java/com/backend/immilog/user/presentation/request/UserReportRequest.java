package com.backend.immilog.user.presentation.request;

import com.backend.immilog.user.application.command.UserReportCommand;
import com.backend.immilog.user.domain.enums.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 신고 요청 DTO")
public record UserReportRequest(
        @Schema(description = "신고 사유", example = "SPAM") ReportReason reason,
        @Schema(description = "신고 내용", example = "신고 내용") String description
) {
    public UserReportCommand toCommand() {
        return new UserReportCommand(
                this.reason,
                this.description
        );
    }
}
