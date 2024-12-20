package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.global.infrastructure.persistence.repository.RedisDataRepository;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.repositories.PopularPostRepository;
import com.backend.immilog.post.infrastructure.querydsl.PostQueryDslRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PopularPostRepositoryImpl implements PopularPostRepository {
    private final PostQueryDslRepository postQueryDslRepository;
    private final RedisDataRepository redisDataRepository;
    private final ObjectMapper objectMapper;

    public PopularPostRepositoryImpl(
            PostQueryDslRepository postQueryDslRepository,
            RedisDataRepository redisDataRepository,
            ObjectMapper objectMapper
    ) {
        this.postQueryDslRepository = postQueryDslRepository;
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
        return postQueryDslRepository.getPopularPosts(
                from,
                to,
                SortingMethods.VIEW_COUNT
        );
    }

    @Override
    public List<PostResult> getHotPosts(
            LocalDateTime from,
            LocalDateTime to
    ) {
        return postQueryDslRepository.getPopularPosts(
                from,
                to,
                SortingMethods.COMMENT_COUNT
        );
    }
}
