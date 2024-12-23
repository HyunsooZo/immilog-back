package com.backend.immilog.global.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum CommonErrorCode implements ErrorCode {

    UNDEFINED_EXCEPTION(BAD_REQUEST, "알 수 없는 오류가 발생하였습니다."),
    IMAGE_UPLOAD_FAILED(BAD_REQUEST, "이미지 업로드에 실패하였습니다."),
    USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다.");

    private final HttpStatus status;
    private final String message;

    CommonErrorCode(
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
