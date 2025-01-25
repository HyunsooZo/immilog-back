package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostResourceEntity {
    private Long seq;
    private Long postSeq;
    private PostType postType;
    private ResourceType resourceType;
    private String content;

    protected PostResourceEntity() {}

    public PostResourceEntity(
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            String content
    ) {
        this.seq = null;
        this.postSeq = postSeq;
        this.postType = postType;
        this.resourceType = resourceType;
        this.content = content;
    }

    public static PostResourceEntity of(
            PostType postType,
            ResourceType resourceType,
            String content,
            Long postSeq
    ) {
        return new PostResourceEntity(
                postSeq,
                postType,
                resourceType,
                content
        );
    }

}
