package com.backend.immilog.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.backend.immilog.global.exception.CommonErrorCode.UNDEFINED_EXCEPTION;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidException(
            MethodArgumentNotValidException exception
    ) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(400).body(errors);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException exception
    ) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(errorResponse.errorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> undefinedException(
            Exception exception
    ) {
        ErrorResponse errorResponse = new ErrorResponse(UNDEFINED_EXCEPTION, exception.getMessage());
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(errorResponse.errorCode().getStatus()).body(errorResponse);
    }
}
