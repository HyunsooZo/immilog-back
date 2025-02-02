package com.backend.immilog.post.application.command;

import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시물 생성 요청 서비스 DTO")
public record JobBoardUploadCommand(
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
}
