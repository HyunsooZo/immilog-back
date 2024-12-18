package com.backend.immilog.post.domain.enums;

import com.backend.immilog.post.exception.PostException;

import java.util.Arrays;

import static com.backend.immilog.post.exception.PostErrorCode.INVALID_REFERENCE_TYPE;

public enum ReferenceType {
    COMMENT,
    POST;
    public static ReferenceType getByString(String referenceType) {
        return Arrays.stream(ReferenceType.values())
                .filter(type -> type.name().compareToIgnoreCase(referenceType) == 0)
                .findFirst()
                .orElseThrow(() -> new PostException(INVALID_REFERENCE_TYPE));
    }
}
