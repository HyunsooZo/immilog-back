package com.backend.immilog.notice.presentation.request;

import com.backend.immilog.notice.application.command.NoticeModifyCommand;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;

public record NoticeModifyRequest(
        String title,
        String content,
        NoticeType type,
        NoticeStatus status
) {
    public NoticeModifyCommand toCommand() {
        return new NoticeModifyCommand(title, content, type, status);
    }
}
