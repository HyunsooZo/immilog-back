package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.services.CommentCommandService;
import com.backend.immilog.post.application.services.PostCommandService;
import com.backend.immilog.post.application.services.PostQueryService;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.comment.ReferenceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface CommentUploadUseCase {
    void uploadComment(
            Long userId,
            Long postSeq,
            String referenceType,
            String content
    );

    @Slf4j
    @Service
    class CommentUploader implements CommentUploadUseCase {
        private final CommentCommandService commentCommandService;
        private final PostQueryService postQueryService;
        private final PostCommandService postCommandService;

        public CommentUploader(
                CommentCommandService commentCommandService,
                PostQueryService postQueryService,
                PostCommandService postCommandService
        ) {
            this.commentCommandService = commentCommandService;
            this.postQueryService = postQueryService;
            this.postCommandService = postCommandService;
        }

        @Transactional
        public void uploadComment(
                Long userId,
                Long postSeq,
                String referenceType,
                String content
        ) {
            final var post = postQueryService.getPostById(postSeq);
            final var deletedPost = post.increaseCommentCount();
            final var reference = ReferenceType.getByString(referenceType);
            final var comment = Comment.of(userId, postSeq, content, reference);
            postCommandService.save(deletedPost);
            commentCommandService.save(comment);
        }
    }
}