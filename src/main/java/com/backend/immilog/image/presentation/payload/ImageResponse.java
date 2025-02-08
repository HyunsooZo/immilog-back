package com.backend.immilog.image.presentation.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;

public record ImageResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "이미지 경로 목록", example = "[\"imagePath1\", \"imagePath2\"]") List<String> data
) {
    public static ImageResponse of(List<String> data) {
        return new ImageResponse(
                HttpStatus.OK.value(),
                "success",
                data
        );
    }

    public static ImageResponse success() {
        return new ImageResponse(
                HttpStatus.NO_CONTENT.value(),
                "success",
                List.of()
        );
    }
}
