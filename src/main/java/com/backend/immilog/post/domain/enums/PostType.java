package com.backend.immilog.post.domain.enums;

import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;

import java.util.Arrays;

public enum PostType {
    POST("posts"),
    JOB_BOARD("jobboards");

    private final String postType;

    PostType(String postType) {
        this.postType = postType;
    }

    public static PostType convertToEnum(String postType) {
        return Arrays.stream(PostType.values())
                .filter(type -> postType.compareToIgnoreCase(type.name()) == 0)
                .findFirst()
                .orElseThrow(() -> new PostException(PostErrorCode.INVALID_POST_TYPE));
    }
}
