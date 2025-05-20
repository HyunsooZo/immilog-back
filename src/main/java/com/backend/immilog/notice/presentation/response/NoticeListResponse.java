package com.backend.immilog.notice.presentation.response;

import com.backend.immilog.notice.application.dto.NoticeResult;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public record NoticeListResponse(
        @Schema(description = "상태 코드", example = "200")
        Integer status,
        @Schema(description = "메시지", example = "success")
        String message,
        @Schema(description = "공지사항 목록")
        Page<NoticeResult> data
) {
    public static NoticeListResponse of(Page<NoticeResult> notices) {
        if(notices.isEmpty()) {
            return new NoticeListResponse(HttpStatus.NO_CONTENT.value(), "공지사항이 존재하지 않습니다.", null);
        }
        return new NoticeListResponse(HttpStatus.OK.value(), "success", notices);
    }
}
