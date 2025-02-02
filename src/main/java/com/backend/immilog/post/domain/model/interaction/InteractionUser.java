package com.backend.immilog.post.domain.model.interaction;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;

public record InteractionUser(
        Long seq,
        Long postSeq,
        PostType postType,
        InteractionType interactionType,
        Long userSeq
) {
    public static InteractionUser of(
            Long postSeq,
            PostType postType,
            InteractionType interactionType,
            Long userSeq
    ) {
        return new InteractionUser(
                null,
                postSeq,
                postType,
                interactionType,
                userSeq
        );
    }
}
