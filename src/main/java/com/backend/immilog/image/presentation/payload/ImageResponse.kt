package com.backend.immilog.image.presentation.payload

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "이미지 응답 DTO")
data class ImageResponse(
    @Schema(description = "상태 코드", example = "200")
    val status: Int,

    @Schema(description = "메시지", example = "success")
    val message: String,

    @Schema(description = "이미지 경로 목록", example = "[\"imagePath1\", \"imagePath2\"]")
    val data: List<String>
) {
    companion object {
        fun of(data: List<String>) = run {
            ImageResponse(
                status = HttpStatus.OK.value(),
                message = "success",
                data = data
            )
        }

        fun success() = run {
            ImageResponse(
                status = HttpStatus.NO_CONTENT.value(),
                message = "success",
                data = emptyList()
            )
        }
    }
}
