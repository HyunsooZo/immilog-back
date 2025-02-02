package com.backend.immilog.post.presentation.response;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public record PostApiResponse(
        Integer status,
        String message,
        Object data,
        List<Object> list
) {
    public static PostApiResponse of(Object data) {
        return new PostApiResponse(
                HttpStatus.OK.value(),
                "success",
                data,
                new ArrayList<>()
        );
    }
}
