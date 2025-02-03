package com.backend.immilog.image.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 요청 DTO")
public record ImageRequest(
        String imageDirectory,
        String imagePath
) {
}

