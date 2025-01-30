package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;

import java.util.List;
import java.util.Optional;

public interface InteractionUserRepository {
    List<InteractionUser> getByPostSeq(
            Long postSeq
    );

    void delete(
            InteractionUser interactionUser
    );

    void save(
            InteractionUser likeUser
    );

    Optional<InteractionUser> getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
            Long postSeq,
            Long userSeq,
            PostType postType,
            InteractionType interactionType
    );

    List<InteractionUser> getInteractionsByPostSeqList(
            List<Long> postSeqList,
            PostType postType
    );
}
