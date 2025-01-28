package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.services.command.CommentCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;

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
        Post post = getPost(postSeq);
        Post deletedPost = post.updateCommentCount();
        ReferenceType reference = ReferenceType.getByString(referenceType);
        Comment comment = Comment.of(userId, postSeq, content, reference);
        postCommandService.save(deletedPost);
        commentCommandService.save(comment);
    }

    private Post getPost(Long postSeq) {
        return postQueryService
                .getPostById(postSeq)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }
}
