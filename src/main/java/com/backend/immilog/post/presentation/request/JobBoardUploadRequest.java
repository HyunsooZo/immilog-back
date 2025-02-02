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
        return new JobBoardUploadCommand(
                this.seq,
                this.title,
                this.content,
                this.viewCount,
                this.likeCount,
                this.tags,
                this.attachments,
                this.deadline,
                this.experience,
                this.salary,
                this.companySeq,
                this.status
        );
    }
}
