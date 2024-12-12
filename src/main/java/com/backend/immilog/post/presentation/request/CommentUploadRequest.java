package com.backend.immilog.post.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "댓글 업로드 요청 DTO")
public record CommentUploadRequest(
        @NotNull(message = "댓글 내용을 입력해주세요.") String content
) {
}
