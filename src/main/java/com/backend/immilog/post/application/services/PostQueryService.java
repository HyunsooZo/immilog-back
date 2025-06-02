package com.backend.immilog.post.application.services;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import com.backend.immilog.post.application.mapper.PostResultAssembler;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.post.SortingMethods;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class PostQueryService {
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;
    private final DataRepository redisDataRepository;
    private final InteractionUserQueryService interactionUserQueryService;
    private final PostResourceQueryService postResourceQueryService;
    private final PostResultAssembler postResultAssembler;

    public PostQueryService(
            ObjectMapper objectMapper,
            PostRepository postRepository,
            DataRepository redisDataRepository,
            InteractionUserQueryService interactionUserQueryService,
            PostResourceQueryService postResourceQueryService,
            PostResultAssembler postResultAssembler
    ) {
        this.objectMapper = objectMapper;
        this.postRepository = postRepository;
        this.redisDataRepository = redisDataRepository;
        this.interactionUserQueryService = interactionUserQueryService;
        this.postResourceQueryService = postResourceQueryService;
        this.postResultAssembler = postResultAssembler;
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long postSeq) {
        return postRepository.getById(postSeq);
    }

    @PerformanceMonitor
    @Transactional(readOnly = true)
    public Page<PostResult> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        var posts = postRepository.getPosts(country, sortingMethod, isPublic, category, pageable);
        var postSeqList = posts.stream().map(Post::seq).toList();
        var postResults = posts.map(Post::toResult);
        return this.assemblePostResult(postSeqList, postResults);
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        var posts = postRepository.getPostsByKeyword(keyword, pageable);
        var postSeqList = posts.stream().map(Post::seq).toList();
        var postResults = posts.map(Post::toResult);
        var updatedPostResultsPage = new PageImpl<>(
                postResults.getContent().stream().map(post -> postResultAssembler.assembleKeywords(post, keyword)).toList(),
                pageable,
                postResults.getTotalElements()
        );
        return this.assemblePostResult(postSeqList, updatedPostResultsPage);
    }

    @Transactional(readOnly = true)
    public PostResult getPostDetail(Long postSeq) {
        var posts = new PageImpl<>(List.of(postRepository.getById(postSeq)));
        var postResult = posts.map(Post::toResult);
        return this.assemblePostResult(List.of(postSeq), postResult).getContent().getFirst();
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        var posts = postRepository.getPostsByUserSeq(userSeq, pageable);
        var postResults = posts.map(Post::toResult);
        return this.assemblePostResult(posts.stream().map(Post::seq).toList(), postResults);
    }

    public List<PostResult> getPostsFromRedis(String key) {
        var jsonData = redisDataRepository.findByKey(key);
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

    public List<PostResult> getPostsByPostSeqList(List<Long> postSeqList) {
        var postResults = postRepository.getPostsByPostSeqList(postSeqList).stream().map(Post::toResult).toList();
        return this.assemblePostResult(postSeqList, new PageImpl<>(postResults)).toList();
    }

    private Page<PostResult> assemblePostResult(
            List<Long> resultSeqList,
            Page<PostResult> postResults
    ) {
        var orderMap = IntStream.range(0, resultSeqList.size())
                .boxed()
                .collect(Collectors.toMap(resultSeqList::get, i -> i));

        var interactionUsers = interactionUserQueryService.getInteractionUsersByPostSeqList(resultSeqList, PostType.POST);
        var postResources = postResourceQueryService.getResourcesByPostSeqList(resultSeqList, PostType.POST);

        return postResults.map(postResult -> {
            var resources = postResources.stream()
                    .filter(postResource -> postResource.postSeq().equals(postResult.seq()))
                    .sorted(Comparator.comparingInt(pr -> orderMap.getOrDefault(pr.postSeq(), Integer.MAX_VALUE)))
                    .toList();

            var interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.postSeq().equals(postResult.seq()))
                    .sorted(Comparator.comparingInt(iu -> orderMap.getOrDefault(iu.postSeq(), Integer.MAX_VALUE)))
                    .toList();

            var postResultWithNewInteractionUsers = postResultAssembler.assembleInteractionUsers(postResult, interactionUserList);
            var postResultWithNewResources = postResultAssembler.assembleResources(postResultWithNewInteractionUsers, resources);
            return postResultAssembler.assembleLikeCount(postResultWithNewResources,interactionUserList.size());
        });
    }
}
