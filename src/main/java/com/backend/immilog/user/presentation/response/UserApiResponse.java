package com.backend.immilog.user.presentation.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public record UserApiResponse(
        Integer status,
        String message,
        Object data,
        List<Object> list
) {
    public static UserApiResponse of(Object data) {
        return new UserApiResponse(
                HttpStatus.OK.value(),
                null,
                data,
                null
        );
    }
}
