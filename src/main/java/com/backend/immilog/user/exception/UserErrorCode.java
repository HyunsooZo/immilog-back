package com.backend.immilog.user.exception;

import com.backend.immilog.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다."),
    EXISTING_USER(BAD_REQUEST, "이미 존재하는 사용자입니다."),
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_STATUS_NOT_ACTIVE(BAD_REQUEST, "사용자 상태가 활성화되어 있지 않습니다."),
    ALREADY_REPORTED(BAD_REQUEST, "이미 신고한 사용자입니다."),
    CANNOT_REPORT_MYSELF(BAD_REQUEST, "자기 자신은 신고할 수 없습니다."),
    NOT_AN_ADMIN_USER(BAD_REQUEST, "관리자 권한이 없는 사용자입니다."),
    EMAIL_SEND_FAILED(BAD_REQUEST, "이메일 발송에 실패하였습니다."),
    ENTITY_TO_DOMAIN_ERROR(BAD_REQUEST, "Entity를 Domain으로 변환하는데 실패하였습니다.");

    private final HttpStatus status;
    private final String message;

    UserErrorCode(
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
