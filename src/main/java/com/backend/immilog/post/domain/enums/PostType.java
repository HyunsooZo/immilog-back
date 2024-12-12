package com.backend.immilog.post.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {
    POST("post"),
    JOB_BOARD("job_board");

    private final String value;

    public static PostType convertToEnum(
            String postType
    ) {
        return switch (postType) {
            case "post" -> POST;
            case "job_board" -> JOB_BOARD;
            default -> throw new IllegalStateException("Unexpected value: " + postType);
        };
    }
}
