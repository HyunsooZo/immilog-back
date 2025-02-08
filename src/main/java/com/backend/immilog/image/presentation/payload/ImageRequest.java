package com.backend.immilog.image.presentation.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 요청 DTO")
public record ImageRequest(
        @Schema(description = "이미지 디렉토리 명", example = "image") String imageDirectory,
        @Schema(description = "이미지 경로", example = "imagePath") String imagePath
) {
}

