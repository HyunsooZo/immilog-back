package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PopularPostRepository;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PostCommandService {
    private final PostRepository postRepository;
    private final PopularPostRepository popularPostRepository;

    public PostCommandService(
            PostRepository postRepository,
            PopularPostRepository popularPostRepository
    ) {
        this.postRepository = postRepository;
        this.popularPostRepository = popularPostRepository;
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void saveMostViewedPosts(
            List<PostResult> posts,
            Integer expiration
    ) throws JsonProcessingException {
        if (posts.isEmpty()) {
            log.warn("[POPULAR POST AGGREGATION FAILED] Failed to save most viewed posts");
            return;
        }
        popularPostRepository.saveMostViewedPosts(posts, expiration);
    }

    @Transactional
    public void saveHotPosts(
            List<PostResult> popularPosts,
            int expiration
    ) throws JsonProcessingException {
        if (popularPosts.isEmpty()) {
            log.warn("[POPULAR POST AGGREGATION FAILED] Failed to save hot posts");
            return;
        }
        popularPostRepository.saveHotPosts(popularPosts, expiration);
    }

}
