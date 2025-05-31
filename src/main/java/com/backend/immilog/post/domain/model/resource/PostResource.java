package com.backend.immilog.post.domain.model.resource;

import com.backend.immilog.post.domain.model.post.PostType;

public class PostResource {
    private final Long seq;
    private final Long postSeq;
    private final PostType postType;
    private final ResourceType resourceType;
    private final String content;

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
        return new PostResource(null, postSeq, postType, resourceType, content);
    }

    public Long seq() {return seq;}

    public Long postSeq() {return postSeq;}

    public PostType postType() {return postType;}

    public ResourceType resourceType() {return resourceType;}

    public String content() {return content;}
}
