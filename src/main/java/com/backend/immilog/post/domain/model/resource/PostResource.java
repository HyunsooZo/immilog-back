package com.backend.immilog.post.domain.model.resource;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResource {
    private final Long seq;
    private final Long postSeq;
    private final PostType postType;
    private final ResourceType resourceType;
    private final String content;

    @Builder
    public PostResource(
            Long seq,
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            String content
    ) {
        this.seq = seq;
        this.postSeq = postSeq;
        this.postType = postType;
        this.resourceType = resourceType;
        this.content = content;
    }

    public static PostResource of(
            PostType postType,
            ResourceType resourceType,
            String content,
            Long postSeq
    ) {
        return PostResource.builder()
                .postType(postType)
                .resourceType(resourceType)
                .content(content)
                .postSeq(postSeq)
                .build();
    }

}
