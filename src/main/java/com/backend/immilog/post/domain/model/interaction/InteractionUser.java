package com.backend.immilog.post.domain.model.interaction;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InteractionUser {
    private final Long seq;
    private final Long postSeq;
    private final PostType postType;
    private final InteractionType interactionType;
    private final Long userSeq;

    @Builder
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
        return InteractionUser.builder()
                .postSeq(postSeq)
                .postType(postType)
                .interactionType(interactionType)
                .userSeq(userSeq)
                .build();
    }
}
