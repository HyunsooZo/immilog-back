package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.services.InteractionUserCommandService;
import com.backend.immilog.post.application.services.InteractionUserQueryService;
import com.backend.immilog.post.domain.model.interaction.InteractionType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.PostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface InteractionCreateUseCase {
    void createInteraction(
            Long userSeq,
            Long postSeq,
            PostType postType,
            InteractionType interactionType
    );

    @Slf4j
    @Service
    class InteractionCreator implements InteractionCreateUseCase {
    private final InteractionUserCommandService interactionUserCommandService;
    private final InteractionUserQueryService interactionUserQueryService;

        public InteractionCreator(
            InteractionUserCommandService interactionUserCommandService,
            InteractionUserQueryService interactionUserQueryService
    ) {
        this.interactionUserCommandService = interactionUserCommandService;
        this.interactionUserQueryService = interactionUserQueryService;
    }

        @Transactional
    public void createInteraction(
            Long userSeq,
            Long postSeq,
            PostType postType,
            InteractionType interactionType
    ) {
            interactionUserQueryService.getInteraction(postSeq, userSeq, postType, interactionType)
                .ifPresentOrElse(
                        interactionUserCommandService::delete,
                        () -> interactionUserCommandService.save(InteractionUser.of(postSeq, postType, interactionType, userSeq))
                );
        }
    }
}
