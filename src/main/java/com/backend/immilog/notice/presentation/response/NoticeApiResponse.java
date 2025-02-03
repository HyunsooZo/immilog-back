package com.backend.immilog.notice.presentation.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public record NoticeApiResponse(
        Integer status,
        String message,
        Object data,
        List<Object> list
) {
    public static NoticeApiResponse of(Object data) {
        return new NoticeApiResponse(
                HttpStatus.OK.value(),
                null,
                data,
                null
        );
    }
}