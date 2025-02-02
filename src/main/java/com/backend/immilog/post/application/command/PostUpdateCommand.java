package com.backend.immilog.post.application.command;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시물 수정 요청 Service DTO")
public record PostUpdateCommand(
        String title,
        String content,
        List<String> deleteTags,
        List<String> addTags,
        List<String> deleteAttachments,
        List<String> addAttachments,
        Boolean isPublic
) {
}


