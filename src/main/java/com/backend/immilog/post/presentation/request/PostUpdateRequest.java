package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.PostUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시물 수정 요청 DTO")
public record PostUpdateRequest(
        String title,
        String content,
        List<String> deleteTags,
        List<String> addTags,
        List<String> deleteAttachments,
        List<String> addAttachments,
        Boolean isPublic
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
