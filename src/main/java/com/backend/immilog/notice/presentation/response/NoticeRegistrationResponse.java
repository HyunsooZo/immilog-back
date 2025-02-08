package com.backend.immilog.notice.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record NoticeRegistrationResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "null") String message,
        @Schema(description = "읽지 않은 공지사항 존재 여부", example = "true | false") Boolean data
) {
    public static NoticeRegistrationResponse success() {
        return new NoticeRegistrationResponse(HttpStatus.OK.value(), "success", Boolean.TRUE);
    }

    public static NoticeRegistrationResponse of(Boolean unreadNoticeExist) {
        return new NoticeRegistrationResponse(HttpStatus.OK.value(), "success", unreadNoticeExist);
    }
}