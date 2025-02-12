package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record JobBoardUploadRequest(
        @Schema(description = "게시물 번호", example = "1") Long seq,
        @Schema(description = "게시물 제목", example = "게시물 제목") String title,
        @Schema(description = "게시물 내용", example = "게시물 내용") String content,
        @Schema(description = "조회수", example = "0") Long viewCount,
        @Schema(description = "좋아요 수", example = "0") Long likeCount,
        @Schema(description = "태그 리스트", example = "[\"태그1\", \"태그2\"]") List<String> tags,
        @Schema(description = "첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]") List<String> attachments,
        @Schema(description = "마감일", example = "2021-07-01T00:00:00") LocalDateTime deadline,
        @Schema(description = "경력", example = "JUNIOR") Experience experience,
        @Schema(description = "급여", example = "3000만원") String salary,
        @Schema(description = "회사 번호", example = "1") Long companySeq,
        @Schema(description = "게시물 상태", example = "ACTIVE") PostStatus status
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
