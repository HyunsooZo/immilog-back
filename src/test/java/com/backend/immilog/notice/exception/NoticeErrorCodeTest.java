package com.backend.immilog.notice.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@DisplayName("Enum NoticeErrorCode 테스트")
class NoticeErrorCodeTest {

    @Test
    @DisplayName("Enum NoticeErrorCode의 getStatus() 메서드 테스트")
    void getStatus_returnsCorrectHttpGetStatus() {
        Assertions.assertThat(BAD_REQUEST).isEqualTo(NoticeErrorCode.NOT_AN_ADMIN_USER.getStatus());
        Assertions.assertThat(NOT_FOUND).isEqualTo(NoticeErrorCode.NOTICE_NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("Enum NoticeErrorCode의 getMessage() 메서드 테스트")
    void getMessage_returnsCorrectMessage() {
        Assertions.assertThat("관리자 권한이 없는 사용자입니다.")
                .isEqualTo(NoticeErrorCode.NOT_AN_ADMIN_USER.getMessage());
        Assertions.assertThat("존재하지 않는 공지사항입니다.")
                .isEqualTo(NoticeErrorCode.NOTICE_NOT_FOUND.getMessage());
    }
}