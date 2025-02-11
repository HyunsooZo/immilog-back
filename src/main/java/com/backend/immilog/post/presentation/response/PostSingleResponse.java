package com.backend.immilog.post.presentation.response;

import com.backend.immilog.post.application.result.PostResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostSingleResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "게시글") PostResult data
) {
}
