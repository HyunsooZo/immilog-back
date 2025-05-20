package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.notice.application.dto.NoticeUploadCommand;

public interface NoticeCreateUseCase {
    void createNotice(
            String token,
            NoticeUploadCommand command
    );
}
