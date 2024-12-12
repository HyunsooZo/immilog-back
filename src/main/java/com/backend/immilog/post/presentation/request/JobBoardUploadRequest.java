package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record JobBoardUploadRequest(
        Long seq,
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        List<String> tags,
        List<String> attachments,
        LocalDateTime deadline,
        Experience experience,
        String salary,
        Long companySeq,
        PostStatus status
) {
    public JobBoardUploadCommand toCommand() {
        return JobBoardUploadCommand.builder()
                .seq(seq)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .tags(tags)
                .attachments(attachments)
                .deadline(deadline)
                .experience(experience)
                .salary(salary)
                .companySeq(companySeq)
                .status(status)
                .build();
    }
}
