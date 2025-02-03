package com.backend.immilog.global.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {
}
