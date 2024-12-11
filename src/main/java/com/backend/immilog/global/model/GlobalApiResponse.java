package com.backend.immilog.global.model;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record GlobalApiResponse(
        Integer status,
        String message,
        Object data,
        List<Object> list
) {
    public static GlobalApiResponse of(Object data) {
        return GlobalApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(null)
                .data(data)
                .list(null)
                .build();
    }
}
