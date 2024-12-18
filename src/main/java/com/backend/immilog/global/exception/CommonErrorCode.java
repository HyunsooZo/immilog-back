package com.backend.immilog.global.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum CommonErrorCode implements ErrorCode {

    UNDEFINED_EXCEPTION(BAD_REQUEST, "알 수 없는 오류가 발생하였습니다."),
    IMAGE_UPLOAD_FAILED(BAD_REQUEST, "이미지 업로드에 실패하였습니다.");

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
