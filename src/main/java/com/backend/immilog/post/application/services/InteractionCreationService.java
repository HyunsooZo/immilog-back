package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.services.command.InteractionUserCommandService;
import com.backend.immilog.post.application.services.query.InteractionUserQueryService;
import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class InteractionCreationService {
    private final InteractionUserCommandService interactionUserCommandService;
    private final InteractionUserQueryService interactionUserQueryService;

    public InteractionCreationService(
            InteractionUserCommandService interactionUserCommandService,
            InteractionUserQueryService interactionUserQueryService
    ) {
        this.interactionUserCommandService = interactionUserCommandService;
        this.interactionUserQueryService = interactionUserQueryService;
    }

    @Async
    @Transactional
    public void createInteraction(
            Long userSeq,
            Long postSeq,
            String post,
            String interaction
    ) {
        PostType postType = PostType.convertToEnum(post);
        InteractionType interactionType = InteractionType.convertToEnum(interaction);
        interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
                        postSeq,
                        userSeq,
                        postType,
                        interactionType
                )
                .ifPresentOrElse(
                        interactionUserCommandService::delete,
                        () -> {
                            interactionUserCommandService.save(
                                    InteractionUser.of(
                                            postSeq,
                                            postType,
                                            interactionType,
                                            userSeq
                                    )
                            );
                        }
                );
    }
}
