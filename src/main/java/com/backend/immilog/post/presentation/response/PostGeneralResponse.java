package com.backend.immilog.post.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record PostGeneralResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message
) {
    public static PostGeneralResponse noContent() {
        return new PostGeneralResponse(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }
}
