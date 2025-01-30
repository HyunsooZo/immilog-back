package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class PostDeleteService {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final PostResourceCommandService postResourceCommandService;

    public PostDeleteService(
            PostCommandService postCommandService,
            PostQueryService postQueryService,
            PostResourceCommandService postResourceCommandService
    ) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
        this.postResourceCommandService = postResourceCommandService;
    }

    @Transactional
    public void deletePost(
            Long userId,
            Long postSeq
    ) {
        Post post = postQueryService.getPostById(postSeq);
        if (!Objects.equals(post.getUserSeq(), userId)) {
            throw new PostException(PostErrorCode.NO_AUTHORITY);
        }
        Post deletedPost = post.delete();
        postCommandService.save(deletedPost);
        postResourceCommandService.deleteAllByPostSeq(deletedPost.getSeq());
    }

}
