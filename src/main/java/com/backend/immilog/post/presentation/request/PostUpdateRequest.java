package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.PostUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시물 수정 요청 DTO")
public record PostUpdateRequest(
        @Schema(description = "게시글 제목", example = "게시글 제목") String title,
        @Schema(description = "게시글 내용", example = "게시글 내용") String content,
        @Schema(description = "삭제할 태그 리스트", example = "[\"태그1\", \"태그2\"]") List<String> deleteTags,
        @Schema(description = "추가할 태그 리스트", example = "[\"태그1\", \"태그2\"]") List<String> addTags,
        @Schema(description = "삭제할 첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]") List<String> deleteAttachments,
        @Schema(description = "추가할 첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]") List<String> addAttachments,
        @Schema(description = "공개 여부", example = "true") Boolean isPublic
) {
    public PostUpdateCommand toCommand() {
        return new PostUpdateCommand(
                title,
                content,
                deleteTags,
                addTags,
                deleteAttachments,
                addAttachments,
                isPublic
        );
    }
}
