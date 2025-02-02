package com.backend.immilog.post.domain.model.resource;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;

public record PostResource(
        Long seq,
        Long postSeq,
        PostType postType,
        ResourceType resourceType,
        String content
) {
    public static PostResource of(
            PostType postType,
            ResourceType resourceType,
            String content,
            Long postSeq
    ) {
        return new PostResource(
                null,
                postSeq,
                postType,
                resourceType,
                content
        );
    }
}
