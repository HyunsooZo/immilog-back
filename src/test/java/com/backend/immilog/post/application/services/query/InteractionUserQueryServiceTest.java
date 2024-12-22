package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("InteractionUserQueryService 테스트")
class InteractionUserQueryServiceTest {

    private final InteractionUserRepository interactionUserRepository = mock(InteractionUserRepository.class);
    private final InteractionUserQueryService interactionUserQueryService = new InteractionUserQueryService(interactionUserRepository);

    @Test
    @DisplayName("InteractionUser를 성공적으로 반환")
    void getByPostSeqAndUserSeqAndPostTypeAndInteractionTypeReturnsInteractionUserSuccessfully() {
        Long postSeq = 1L;
        Long userSeq = 1L;
        PostType postType = PostType.POST;
        InteractionType interactionType = InteractionType.LIKE;
        InteractionUser expectedInteractionUser = new InteractionUser(
                1L,
                postSeq,
                postType,
                interactionType,
                userSeq
        );
        when(interactionUserRepository.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(postSeq, userSeq, postType, interactionType))
                .thenReturn(Optional.of(expectedInteractionUser));

        Optional<InteractionUser> actualInteractionUser = interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(postSeq, userSeq, postType, interactionType);

        assertThat(actualInteractionUser).isPresent();
        assertThat(actualInteractionUser.get()).isEqualTo(expectedInteractionUser);
    }

    @Test
    @DisplayName("메서드가 빈 Optional을 반환")
    void getByPostSeqAndUserSeqAndPostTypeAndInteractionTypeReturnsEmptyOptional() {
        Long postSeq = 1L;
        Long userSeq = 1L;
        PostType postType = PostType.POST;
        InteractionType interactionType = InteractionType.LIKE;
        when(interactionUserRepository.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(postSeq, userSeq, postType, interactionType))
                .thenReturn(Optional.empty());

        Optional<InteractionUser> actualInteractionUser = interactionUserQueryService.getByPostSeqAndUserSeqAndPostTypeAndInteractionType(postSeq, userSeq, postType, interactionType);

        assertThat(actualInteractionUser).isEmpty();
    }
}