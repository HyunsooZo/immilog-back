package com.backend.immilog.notice.application.dto;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.NoticeType;

import java.util.List;

public record NoticeUploadCommand(
        String title,
        String content,
        NoticeType type,
        List<Country> targetCountry
) {
}
