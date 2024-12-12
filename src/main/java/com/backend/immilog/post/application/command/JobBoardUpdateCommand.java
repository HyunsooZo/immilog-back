package com.backend.immilog.post.application.command;

import com.backend.immilog.post.domain.enums.Experience;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
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
