package com.backend.immilog.post.application.services;

import com.backend.immilog.global.infrastructure.persistence.lock.RedisDistributedLock;
import com.backend.immilog.post.application.services.command.InteractionUserCommandService;
import com.backend.immilog.post.application.services.query.InteractionUserQueryService;
import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class InteractionCreationService {
    final String LIKE_LOCK_KEY = "interaction : ";
    private final InteractionUserCommandService interactionUserCommandService;
    private final InteractionUserQueryService interactionUserQueryService;
    private final RedisDistributedLock redisDistributedLock;

    public InteractionCreationService(
            InteractionUserCommandService interactionUserCommandService,
            InteractionUserQueryService interactionUserQueryService,
            RedisDistributedLock redisDistributedLock
    ) {
        this.interactionUserCommandService = interactionUserCommandService;
        this.interactionUserQueryService = interactionUserQueryService;
        this.redisDistributedLock = redisDistributedLock;
    }

    private static InteractionUser createInteractionUser(
            Long userSeq,
            Long postSeq,
            PostType postType,
            InteractionType interactionType
    ) {
        return InteractionUser.of(postSeq, postType, interactionType, userSeq);
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
        executeWithLock(
                LIKE_LOCK_KEY,
                postSeq.toString(),
                () -> {
                    getInteractionUser(postSeq, userSeq, postType, interactionType)
                            .ifPresentOrElse(
                                    interactionUserCommandService::delete,
                                    () -> {
                                        InteractionUser interactionUser = createInteractionUser(
                                                userSeq,
                                                postSeq,
                                                postType,
                                                interactionType
                                        );
                                        interactionUserCommandService.save(interactionUser);
                                    }
                            );
                }
        );
    }

    private void executeWithLock(
            String lockKey,
            String subKey,
            Runnable action
    ) {
        boolean lockAcquired = false;
        try {
            lockAcquired = redisDistributedLock.tryAcquireLock(lockKey, subKey);
            if (lockAcquired) {
                action.run();
            } else {
                log.error("Failed to acquire lock for {}, key: {}", lockKey, subKey);
            }
        } finally {
            if (lockAcquired) {
                redisDistributedLock.releaseLock(lockKey, subKey);
            }
        }
    }

    private Optional<InteractionUser> getInteractionUser(
            Long postSeq,
            Long userSeq,
            PostType postType,
            InteractionType interactionType
    ) {
        return interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
                postSeq,
                userSeq,
                postType,
                interactionType
        );
    }
}
