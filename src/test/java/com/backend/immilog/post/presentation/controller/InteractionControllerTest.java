package com.backend.immilog.post.presentation.controller;

import com.backend.immilog.post.application.services.InteractionCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

@DisplayName("InteractionController 테스트")
class InteractionControllerTest {
    private final InteractionCreationService interactionCreationService = mock(InteractionCreationService.class);
    private final InteractionController interactionController = new InteractionController(interactionCreationService);

    @Test
    @DisplayName("인터랙션 등록")
    void createInteraction() {
        // given
        String interactionType = "like";
        String postType = "post";
        Long postSeq = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long userSeq = 1L;
        when(request.getAttribute("userSeq")).thenReturn(userSeq);

        // when
        interactionController.createInteraction(interactionType, postType, postSeq, request);

        // then
        verify(interactionCreationService).createInteraction(userSeq, postSeq, postType, interactionType);
    }
}