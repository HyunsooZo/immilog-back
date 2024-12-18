package com.backend.immilog.notice.exception;

import com.backend.immilog.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum NoticeErrorCode implements ErrorCode {
    NOT_AN_ADMIN_USER(BAD_REQUEST, "관리자 권한이 없는 사용자입니다."),
    NOTICE_NOT_FOUND(NOT_FOUND, "존재하지 않는 공지사항입니다.");

    private final HttpStatus status;
    private final String message;

    NoticeErrorCode(
            HttpStatus status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

}
