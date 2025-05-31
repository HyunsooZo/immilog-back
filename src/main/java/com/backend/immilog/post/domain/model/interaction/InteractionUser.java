package com.backend.immilog.post.domain.model.interaction;

import com.backend.immilog.post.domain.model.post.PostType;

public class InteractionUser {
    private final Long seq;
    private final Long postSeq;
    private final PostType postType;
    private final InteractionType interactionType;
    private final Long userSeq;

    public InteractionUser(
            Long seq,
            Long postSeq,
            PostType postType,
            InteractionType interactionType,
            Long userSeq
    ) {
        this.seq = seq;
        this.postSeq = postSeq;
        this.postType = postType;
        this.interactionType = interactionType;
        this.userSeq = userSeq;
    }

    public static InteractionUser of(
            Long postSeq,
            PostType postType,
            InteractionType interactionType,
            Long userSeq
    ) {
        return new InteractionUser(null, postSeq, postType, interactionType, userSeq);
    }

    public Long seq() {return seq;}

    public Long postSeq() {return postSeq;}

    public PostType postType() {return postType;}

    public InteractionType interactionType() {return interactionType;}

    public Long userSeq() {return userSeq;}
}
