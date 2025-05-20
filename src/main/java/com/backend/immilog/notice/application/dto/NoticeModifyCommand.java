package com.backend.immilog.notice.application.dto;

import com.backend.immilog.notice.domain.model.NoticeStatus;
import com.backend.immilog.notice.domain.model.NoticeType;

public record NoticeModifyCommand(
        String title,
        String content,
        NoticeType type,
        NoticeStatus status
) {
}
