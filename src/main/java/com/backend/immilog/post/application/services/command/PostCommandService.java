package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.model.Post;
import com.backend.immilog.post.domain.repositories.PopularPostRepository;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        popularPostRepository.saveMostViewedPosts(posts, expiration);
    }

    @Transactional
    public void saveHotPosts(
            List<PostResult> popularPosts,
            int expiration
    ) throws JsonProcessingException {
        popularPostRepository.saveHotPosts(popularPosts, expiration);
    }

}
