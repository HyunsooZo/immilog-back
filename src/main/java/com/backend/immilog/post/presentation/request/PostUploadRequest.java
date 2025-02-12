package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.PostUploadCommand;
import com.backend.immilog.post.domain.enums.Categories;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "게시물 생성 요청 DTO")
public record PostUploadRequest(
        @NotBlank(message = "제목을 입력해주세요.")
        @Schema(description = "게시글 제목", example = "게시글 제목")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        @Schema(description = "게시글 내용", example = "게시글 내용")
        String content,

        @Schema(description = "태그 리스트", example = "[\"태그1\", \"태그2\"]")
        List<String> tags,

        @Schema(description = "첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]")
        List<String> attachments,

        @NotNull(message = "전체공개 여부를 입력해주세요.")
        @Schema(description = "전체공개 여부", example = "true")
        Boolean isPublic,

        @NotNull(message = "카테고리를 입력해주세요.")
        @Schema(description = "카테고리", example = "WORKING_HOLIDAY")
        Categories category
) {
    public PostUploadCommand toCommand() {
        return new PostUploadCommand(
                title,
                content,
                tags,
                attachments,
                isPublic,
                category
        );
    }
}
