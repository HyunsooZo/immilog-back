package com.backend.immilog.post.application.services;

import com.backend.immilog.global.infrastructure.persistence.lock.RedisDistributedLock;
import com.backend.immilog.post.application.services.command.InteractionUserCommandService;
import com.backend.immilog.post.application.services.query.InteractionUserQueryService;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("InteractionCreationService 테스트")
class InteractionCreationServiceTest {
    private final InteractionUserCommandService interactionUserCommandService = mock(InteractionUserCommandService.class);
    private final InteractionUserQueryService interactionUserQueryService = mock(InteractionUserQueryService.class);
    private final RedisDistributedLock redisDistributedLock = mock(RedisDistributedLock.class);
    private final InteractionCreationService interactionCreationService = new InteractionCreationService(
            interactionUserCommandService,
            interactionUserQueryService,
            redisDistributedLock
    );

    @Test
    @DisplayName("인터랙션 생성 - 성공 : 이미 등록된 인터랙션")
    void interaction_existing() {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        String post = "post";
        String interaction = "like";
        InteractionUser interactionUser = InteractionUser.builder().build();
        when(redisDistributedLock.tryAcquireLock(any(), any())).thenReturn(true);
        when(interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
                any(), any(), any(), any()
        )).thenReturn(Optional.of(interactionUser));
        // when
        interactionCreationService.createInteraction(userSeq, postSeq, post, interaction);

        // then
        verify(interactionUserCommandService).delete(any());
        verify(redisDistributedLock).releaseLock(any(), any());
    }

    @Test
    @DisplayName("인터랙션 생성 - 성공 : 신규 인터랙션")
    void createInteraction_new() {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        String post = "post";
        String interaction = "like";
        when(redisDistributedLock.tryAcquireLock(any(), any())).thenReturn(true);
        when(interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
                any(), any(), any(), any()
        )).thenReturn(Optional.empty());
        // when
        interactionCreationService.createInteraction(userSeq, postSeq, post, interaction);

        // then
        verify(interactionUserCommandService).save(any());
        verify(redisDistributedLock).releaseLock(any(), any());
    }

    @Test
    @DisplayName("인터랙션 생성 - 실패 : 락 획득 실패")
    void createInteraction_lockFail() {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        String post = "post";
        String interaction = "like";

        // 락 획득 실패를 시뮬레이션
        when(redisDistributedLock.tryAcquireLock(any(), any())).thenReturn(false);

        // when
        interactionCreationService.createInteraction(userSeq, postSeq, post, interaction);

        // then
        verify(interactionUserCommandService, never()).save(any());
        verify(interactionUserCommandService, never()).delete(any());
        verify(redisDistributedLock, never()).releaseLock(any(), any());
    }

}