package com.backend.immilog.image.exception;

import com.backend.immilog.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ImageErrorCode implements ErrorCode {
    IMAGE_NOT_FOUND(NOT_FOUND, "이미지를 찾을 수 없습니다."),
    INVALID_IMAGE_PATH(BAD_REQUEST, "이미지 경로가 올바르지 않습니다."),
    INVALID_IMAGE_TYPE(BAD_REQUEST, "이미지 타입이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ImageErrorCode(
            HttpStatus status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
