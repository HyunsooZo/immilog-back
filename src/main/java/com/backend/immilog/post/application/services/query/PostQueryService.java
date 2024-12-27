package com.backend.immilog.post.application.services.query;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.global.infrastructure.persistence.repository.RedisDataRepository;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostQueryService {
    private final PostRepository postRepository;
    private final RedisDataRepository redisDataRepository;
    private final ObjectMapper objectMapper;

    public PostQueryService(
            PostRepository postRepository,
            RedisDataRepository redisDataRepository,
            ObjectMapper objectMapper
    ) {
        this.postRepository = postRepository;
        this.redisDataRepository = redisDataRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long postSeq) {
        return postRepository.getById(postSeq);
    }

    @PerformanceMonitor
    @Transactional(readOnly = true)
    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        return postRepository.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            PageRequest pageRequest
    ) {
        return postRepository.getPostsByKeyword(keyword, pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<PostResult> getPost(Long postSeq) {
        return postRepository.getPost(postSeq);
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        return postRepository.getPostsByUserSeq(userSeq, pageable);
    }

    public List<PostResult> getPostsFromRedis(String key) {
        String jsonData = redisDataRepository.findByKey(key);
        if (jsonData == null) {
            log.info("No data found with key {}", key);
            return List.of();
        }
        try {
            return objectMapper.readValue(jsonData, new TypeReference<List<PostResult>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json data", e);
        } catch (Exception e) {
            log.error("Failed to get popular posts with key {}", key, e);
        }
        return List.of();
    }

}
