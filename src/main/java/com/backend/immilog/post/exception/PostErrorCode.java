package com.backend.immilog.post.exception;

import com.backend.immilog.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum PostErrorCode implements ErrorCode {
    POST_NOT_FOUND(NOT_FOUND, "존재하지 않는 게시물입니다."),
    NO_AUTHORITY(BAD_REQUEST, "본인이 작성한 글만 수정하거나 삭제할 수 있습니다."),
    ALREADY_DELETED_POST(BAD_REQUEST, "이미 삭제된 게시물입니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "존재하지 않는 댓글입니다."),
    JOB_BOARD_NOT_FOUND(NOT_FOUND, "존재하지 않는 구인 게시판입니다."),
    COMPANY_NOT_FOUND(NOT_FOUND, "존재하지 않는 회사입니다."),
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, "존재하지 않는 채팅방입니다."),
    INVALID_USER(BAD_REQUEST, "해당 채팅방에 대한 권한이 없습니다."),
    EMAIL_SEND_FAILED(BAD_REQUEST, "이메일 발송에 실패하였습니다."),
    FAILED_TO_SAVE_POST(BAD_REQUEST, "게시물을 저장하는데 실패하였습니다."),
    INVALID_REFERENCE_TYPE(BAD_REQUEST, "유효하지 않은 참조 타입입니다.");

    private final HttpStatus status;
    private final String message;

    PostErrorCode(
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
