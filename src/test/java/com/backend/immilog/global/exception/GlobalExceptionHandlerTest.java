package com.backend.immilog.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.backend.immilog.global.exception.CommonErrorCode.UNDEFINED_EXCEPTION;
import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler 클래스 테스트")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("유효성 검사 예외 처리 - 성공")
    void handleValidException_returnsBadRequestWithErrors() {
        // 목킹 설정
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("field", "error message")));
        ResponseEntity<List<String>> response = globalExceptionHandler.handleValidException(exception);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsExactly("error message");
    }

    @Test
    @DisplayName("커스텀 예외 처리 - 성공")
    void handleCustomException_returnsErrorResponse() {
        CustomException exception = new CustomException(EXISTING_USER);
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomException(exception);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(EXISTING_USER);
    }

    @Test
    @DisplayName("정의되지 않은 예외 처리 - 성공")
    void undefinedException_returnsUndefinedErrorResponse() {
        Exception exception = new Exception("Undefined error");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.undefinedException(exception);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(UNDEFINED_EXCEPTION);
    }
}