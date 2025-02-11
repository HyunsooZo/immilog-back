package com.backend.immilog.post.presentation.response;

import com.backend.immilog.post.application.result.JobBoardResult;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public record JobBoardPageResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "구인 정보") Page<JobBoardResult> data
) {
    public static JobBoardPageResponse of(Page<JobBoardResult> data) {
        return new JobBoardPageResponse(
                HttpStatus.OK.value(),
                "success",
                data
        );
    }
}
