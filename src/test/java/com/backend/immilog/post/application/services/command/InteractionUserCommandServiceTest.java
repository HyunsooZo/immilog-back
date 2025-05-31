package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.application.services.InteractionUserCommandService;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("InteractionUserCommandService 테스트")
class InteractionUserCommandServiceTest {

    private final InteractionUserRepository interactionUserRepository = mock(InteractionUserRepository.class);
    private final InteractionUserCommandService interactionUserCommandService = new InteractionUserCommandService(interactionUserRepository);

    @Test
    @DisplayName("save 메서드가 InteractionUser를 성공적으로 저장")
    void saveSavesInteractionUserSuccessfully() {
        InteractionUser interactionUser = new InteractionUser(
                1L,
                1L,
                null,
                null,
                1L
        );

        interactionUserCommandService.save(interactionUser);

        ArgumentCaptor<InteractionUser> interactionUserCaptor = ArgumentCaptor.forClass(InteractionUser.class);
        verify(interactionUserRepository).save(interactionUserCaptor.capture());

        assertThat(interactionUserCaptor.getValue()).isEqualTo(interactionUser);
    }

    @Test
    @DisplayName("save 메서드가 null InteractionUser를 처리")
    void saveHandlesNullInteractionUser() {
        InteractionUser interactionUser = null;

        interactionUserCommandService.save(interactionUser);

        ArgumentCaptor<InteractionUser> interactionUserCaptor = ArgumentCaptor.forClass(InteractionUser.class);
        verify(interactionUserRepository).save(interactionUserCaptor.capture());

        assertThat(interactionUserCaptor.getValue()).isNull();
    }

    @Test
    @DisplayName("delete 메서드가 InteractionUser를 성공적으로 삭제")
    void deleteDeletesInteractionUserSuccessfully() {
        InteractionUser interactionUser = new InteractionUser(
                1L,
                1L,
                null,
                null,
                1L
        );

        interactionUserCommandService.delete(interactionUser);

        ArgumentCaptor<InteractionUser> interactionUserCaptor = ArgumentCaptor.forClass(InteractionUser.class);
        verify(interactionUserRepository).delete(interactionUserCaptor.capture());

        assertThat(interactionUserCaptor.getValue()).isEqualTo(interactionUser);
    }

    @Test
    @DisplayName("delete 메서드가 null InteractionUser를 처리")
    void deleteHandlesNullInteractionUser() {
        InteractionUser interactionUser = null;

        interactionUserCommandService.delete(interactionUser);

        ArgumentCaptor<InteractionUser> interactionUserCaptor = ArgumentCaptor.forClass(InteractionUser.class);
        verify(interactionUserRepository).delete(interactionUserCaptor.capture());

        assertThat(interactionUserCaptor.getValue()).isNull();
    }
}