package com.backend.immilog.notice.application.command;

import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import lombok.Builder;

import java.util.List;

@Builder
public record NoticeUploadCommand(
        String title,
        String content,
        NoticeType type,
        List<NoticeCountry> targetCountries
) {
}
