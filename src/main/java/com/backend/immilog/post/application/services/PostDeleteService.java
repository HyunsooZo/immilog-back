package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.model.Post;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;
import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;

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
        Post post = getPost(postSeq);
        validateAuthor(userId, post);
        deletePostAndResources(post);
    }

    private void deletePostAndResources(Post post) {
        post.delete();
        postCommandService.save(post);
        postResourceCommandService.deleteAllByPostSeq(post.getSeq());
    }

    private Post getPost(Long postSeq) {
        return postQueryService.getPostById(postSeq)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    private void validateAuthor(
            Long userId,
            Post post
    ) {
        if (!Objects.equals(post.getUserSeq(), userId)) {
            throw new PostException(NO_AUTHORITY);
        }
    }
}
