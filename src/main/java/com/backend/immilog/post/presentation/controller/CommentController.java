package com.backend.immilog.post.presentation.controller;

import com.backend.immilog.post.application.services.CommentUploadService;
import com.backend.immilog.post.presentation.request.CommentUploadRequest;
import com.backend.immilog.post.presentation.response.PostApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "CommentEntity API", description = "댓글 관련 API")
@RequestMapping("/api/v1/comments")
@RestController
public class CommentController {
    private final CommentUploadService commentUploadService;

    public CommentController(CommentUploadService commentUploadService) {
        this.commentUploadService = commentUploadService;
    }

    @PostMapping("/{referenceType}/{postSeq}/users/{userSeq}")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    public ResponseEntity<PostApiResponse> createComment(
            @PathVariable("referenceType") String referenceType,
            @PathVariable("postSeq") Long postSeq,
            @PathVariable("userSeq") Long userSeq,
            @Valid @RequestBody CommentUploadRequest commentUploadRequest
    ) {
        commentUploadService.uploadComment(
                userSeq,
                postSeq,
                referenceType,
                commentUploadRequest.content()
        );
        return ResponseEntity.status(CREATED).build();
    }
}
