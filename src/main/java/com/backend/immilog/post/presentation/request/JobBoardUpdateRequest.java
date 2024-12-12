package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.domain.enums.Experience;

import java.time.LocalDateTime;
import java.util.List;

public record JobBoardUpdateRequest(
        String title,
        String content,
        List<String> addTags,
        List<String> deleteTags,
        List<String> addAttachments,
        List<String> deleteAttachments,
        LocalDateTime deadline,
        Experience experience,
        String salary
) {
    public JobBoardUpdateCommand toCommand() {
        return JobBoardUpdateCommand.builder()
                .title(title)
                .content(content)
                .addTags(addTags)
                .deleteTags(deleteTags)
                .addAttachments(addAttachments)
                .deleteAttachments(deleteAttachments)
                .deadline(deadline)
                .experience(experience)
                .salary(salary)
                .build();
    }
}
