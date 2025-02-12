package com.backend.immilog.post.presentation.request;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.domain.enums.Experience;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record JobBoardUpdateRequest(
        @Schema(description = "게시물 제목", example = "게시물 제목") String title,
        @Schema(description = "게시물 내용", example = "게시물 내용") String content,
        @Schema(description = "추가할 태그 리스트", example = "[\"태그1\", \"태그2\"]") List<String> addTags,
        @Schema(description = "삭제할 태그 리스트", example = "[\"태그1\", \"태그2\"]") List<String> deleteTags,
        @Schema(description = "추가할 첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]") List<String> addAttachments,
        @Schema(description = "삭제할 첨부파일 리스트", example = "[\"첨부파일1\", \"첨부파일2\"]") List<String> deleteAttachments,
        @Schema(description = "마감일", example = "2021-07-01T00:00:00") LocalDateTime deadline,
        @Schema(description = "경력", example = "JUNIOR") Experience experience,
        @Schema(description = "급여", example = "3000만원") String salary
) {
    public JobBoardUpdateCommand toCommand() {
        return new JobBoardUpdateCommand(
                title,
                content,
                addTags,
                deleteTags,
                addAttachments,
                deleteAttachments,
                deadline,
                experience,
                salary
        );
    }
}
