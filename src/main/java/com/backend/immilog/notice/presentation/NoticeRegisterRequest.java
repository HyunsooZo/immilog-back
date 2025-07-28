package com.backend.immilog.notice.presentation;

import com.backend.immilog.notice.application.dto.NoticeUploadCommand;
import com.backend.immilog.notice.domain.enums.NoticeType;
import com.backend.immilog.user.domain.model.enums.Country;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record NoticeRegisterRequest(
        @Schema(description = "제목", example = "제목") String title,
        @Schema(description = "내용", example = "내용") String content,
        @Schema(description = "공지사항 타입", example = "NOTICE") NoticeType type,
        @Schema(description = "대상 국가", example = "[\"MALAYSIA\"]") List<Country> targetCountry
) {
    public NoticeUploadCommand toCommand() {
        return new NoticeUploadCommand(title, content, type, targetCountry);
    }
}