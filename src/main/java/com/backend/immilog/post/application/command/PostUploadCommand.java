package com.backend.immilog.post.application.command;

import com.backend.immilog.post.domain.enums.Categories;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "게시물 생성 요청 서비스 DTO")
public record PostUploadCommand(
        @NotBlank(message = "제목을 입력해주세요.") String title,
        @NotBlank(message = "내용을 입력해주세요.") String content,
        List<String> tags,
        List<String> attachments,
        @NotNull(message = "전체공개 여부를 입력해주세요.") Boolean isPublic,
        @NotNull(message = "카테고리를 입력해주세요.") Categories category
) {
}
