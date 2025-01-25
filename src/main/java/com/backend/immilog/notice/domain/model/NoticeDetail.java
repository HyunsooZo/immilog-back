package com.backend.immilog.notice.domain.model;

import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;

public record NoticeDetail(
        String title,
        String content,
        NoticeType type,
        NoticeStatus status
) {
    public static NoticeDetail of(
            String title,
            String content,
            NoticeType type,
            NoticeStatus status
    ) {
        return new NoticeDetail(title, content, type, status);
    }
}
