package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.usecase.InteractionCreateUseCase;
import com.backend.immilog.post.domain.model.interaction.InteractionType;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("InteractionCreationService 테스트")
class InteractionCreateUseCaseTest {
    private final InteractionUserCommandService interactionUserCommandService = mock(InteractionUserCommandService.class);
    private final InteractionUserQueryService interactionUserQueryService = mock(InteractionUserQueryService.class);
    private final InteractionCreateUseCase interactionCreateUseCase = new InteractionCreateUseCase.InteractionCreator(
            interactionUserCommandService,
            interactionUserQueryService
    );

    @Test
    @DisplayName("인터랙션 생성 - 성공 : 이미 등록된 인터랙션")
    void interaction_existing() {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        PostType post = PostType.POST;
        InteractionType interaction = InteractionType.LIKE;
        InteractionUser interactionUser = mock(InteractionUser.class);
        when(interactionUserQueryService.getInteraction(any(), any(), any(), any())).thenReturn(Optional.of(interactionUser));
        // when
        interactionCreateUseCase.createInteraction(userSeq, postSeq, post, interaction);
        // then
        verify(interactionUserCommandService).delete(any());
    }

    @Test
    @DisplayName("인터랙션 생성 - 성공 : 신규 인터랙션")
    void createInteraction_new() {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        PostType post = PostType.POST;
        InteractionType interaction = InteractionType.LIKE;
        when(interactionUserQueryService.getInteraction(any(), any(), any(), any())).thenReturn(Optional.empty());
        // when
        interactionCreateUseCase.createInteraction(userSeq, postSeq, post, interaction);
        // then
        verify(interactionUserCommandService).save(any());
    }
}