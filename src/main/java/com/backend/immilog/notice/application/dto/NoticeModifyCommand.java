package com.backend.immilog.notice.application.dto;

import com.backend.immilog.notice.domain.NoticeStatus;
import com.backend.immilog.notice.domain.NoticeType;

public record NoticeModifyCommand(
        String title,
        String content,
        NoticeType type,
        NoticeStatus status
) {
}
