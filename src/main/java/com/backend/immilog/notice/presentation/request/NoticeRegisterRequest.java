package com.backend.immilog.notice.presentation.request;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.command.NoticeUploadCommand;
import com.backend.immilog.notice.domain.model.enums.NoticeType;

import java.util.List;

public record NoticeRegisterRequest(
        String title,
        String content,
        NoticeType type,
        List<Country> targetCountry
) {
    public NoticeUploadCommand toCommand() {
        return new NoticeUploadCommand(title, content, type, targetCountry);
    }
}