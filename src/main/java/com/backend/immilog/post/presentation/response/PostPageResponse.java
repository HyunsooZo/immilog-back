package com.backend.immilog.post.presentation.response;

import com.backend.immilog.post.application.result.PostResult;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public record PostPageResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "게시글 페이지") Page<PostResult> data
) {
    public static PostPageResponse of(Page<PostResult> data) {
        return new PostPageResponse(
                HttpStatus.OK.value(),
                "success",
                data
        );
    }
}
