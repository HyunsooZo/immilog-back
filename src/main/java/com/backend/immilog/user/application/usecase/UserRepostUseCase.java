package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserReportCommand;

public interface UserRepostUseCase {
    void reportUser(
            Long targetUserSeq,
            Long reporterUserSeq,
            UserReportCommand command
    );
}
