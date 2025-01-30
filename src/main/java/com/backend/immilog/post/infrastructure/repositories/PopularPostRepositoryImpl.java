package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.global.infrastructure.persistence.repository.RedisDataRepository;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PopularPostRepository;
import com.backend.immilog.post.infrastructure.jdbc.PostJdbcRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PopularPostRepositoryImpl implements PopularPostRepository {
    private final PostJdbcRepository postJdbcRepository;
    private final RedisDataRepository redisDataRepository;
    private final ObjectMapper objectMapper;

    public PopularPostRepositoryImpl(
            PostJdbcRepository postJdbcRepository,
            RedisDataRepository redisDataRepository,
            ObjectMapper objectMapper
    ) {
        this.postJdbcRepository = postJdbcRepository;
        this.redisDataRepository = redisDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveMostViewedPosts(
            List<PostResult> posts,
            Integer expiration
    ) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(posts);
        String MOST_VIEWED_POSTS_KEY = "most_viewed_posts";
        redisDataRepository.save(MOST_VIEWED_POSTS_KEY, json, expiration);
    }

    @Override
    public void saveHotPosts(
            List<PostResult> popularPosts,
            int expiration
    ) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(popularPosts);
        String HOT_POSTS_KEY = "hot_posts";
        redisDataRepository.save(HOT_POSTS_KEY, json, expiration);
    }

    @Override
    public List<PostResult> getMostViewedPosts(
            LocalDateTime from,
            LocalDateTime to
    ) {
        List<Post> posts = postJdbcRepository.getPopularPosts(
                from,
                to,
                SortingMethods.VIEW_COUNT
        );
        return posts.stream().map(Post::toResult).toList();
    }

    @Override
    public List<PostResult> getHotPosts(
            LocalDateTime from,
            LocalDateTime to
    ) {
        List<Post> posts = postJdbcRepository.getPopularPosts(
                from,
                to,
                SortingMethods.COMMENT_COUNT
        );
        return posts.stream().map(Post::toResult).toList();
    }
}
