package com.backend.immilog.global.exception;

import com.backend.immilog.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponse 클래스 테스트")
class ErrorResponseTest {

    @Test
    @DisplayName("ErrorResponse 객체가 올바르게 생성되는지 테스트")
    void errorResponseIsCreatedCorrectly() {
        UserErrorCode errorCode = UserErrorCode.USER_NOT_FOUND;
        String message = "An error occurred";
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        assertThat(errorResponse.errorCode()).isEqualTo(errorCode);
        assertThat(errorResponse.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("ErrorResponse 객체의 errorCode가 null인 경우")
    void errorResponseHasNullErrorCode() {
        String message = "An error occurred";
        ErrorResponse errorResponse = new ErrorResponse(null, message);
        assertThat(errorResponse.errorCode()).isNull();
        assertThat(errorResponse.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("ErrorResponse 객체의 message가 null인 경우")
    void errorResponseHasNullMessage() {
        UserErrorCode errorCode = UserErrorCode.USER_NOT_FOUND;

        ErrorResponse errorResponse = new ErrorResponse(errorCode, null);
        assertThat(errorResponse.errorCode()).isEqualTo(errorCode);
        assertThat(errorResponse.message()).isNull();
    }
}