package com.backend.immilog.post.presentation.controller;

import com.backend.immilog.post.application.services.InteractionCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Interaction API", description = "북마크/좋아요 관련 API")
@RequestMapping("/api/v1")
@RestController
public class InteractionController {
    private final InteractionCreationService interactionCreationService;

    public InteractionController(InteractionCreationService interactionCreationService) {
        this.interactionCreationService = interactionCreationService;
    }

    @PostMapping("/{interactionType}/posts/{postSeq}/types/{postType}/users/{userSeq}")
    @Operation(summary = "인터랙션 등록", description = "게시물 좋아요/북마크 등록")
    public ResponseEntity<Void> createInteraction(
            @Parameter(description = "인터렉션 타입") @PathVariable("interactionType") String interactionType,
            @Parameter(description = "게시물 타입") @PathVariable("postType") String postType,
            @Parameter(description = "게시물 고유번호") @PathVariable("postSeq") Long postSeq,
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        interactionCreationService.createInteraction(userSeq, postSeq, postType, interactionType);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
