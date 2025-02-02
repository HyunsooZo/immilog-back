package com.backend.immilog.post.application.command;

import com.backend.immilog.post.domain.enums.Experience;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시물 수정 요청 Service DTO")
public record JobBoardUpdateCommand(
        String title,
        String content,
        List<String> deleteTags,
        List<String> addTags,
        List<String> deleteAttachments,
        List<String> addAttachments,
        LocalDateTime deadline,
        Experience experience,
        String salary
) {
}
