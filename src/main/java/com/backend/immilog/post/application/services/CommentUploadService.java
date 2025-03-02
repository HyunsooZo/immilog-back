package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.services.command.CommentCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.post.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentUploadService {
    private final CommentCommandService commentCommandService;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    public CommentUploadService(
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
        final Post post = postQueryService.getPostById(postSeq);
        final Post deletedPost = post.increaseCommentCount();
        final ReferenceType reference = ReferenceType.getByString(referenceType);
        final Comment comment = Comment.of(userId, postSeq, content, reference);
        postCommandService.save(deletedPost);
        commentCommandService.save(comment);
    }

}
