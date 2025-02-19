package com.backend.immilog.post.application.services.query;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class PostQueryService {
    private final PostRepository postRepository;
    private final DataRepository redisDataRepository;
    private final ObjectMapper objectMapper;
    private final InteractionUserQueryService interactionUserQueryService;
    private final PostResourceQueryService postResourceQueryService;

    public PostQueryService(
            PostRepository postRepository,
            DataRepository redisDataRepository,
            ObjectMapper objectMapper,
            InteractionUserQueryService interactionUserQueryService,
            PostResourceQueryService postResourceQueryService
    ) {
        this.postRepository = postRepository;
        this.redisDataRepository = redisDataRepository;
        this.objectMapper = objectMapper;
        this.interactionUserQueryService = interactionUserQueryService;
        this.postResourceQueryService = postResourceQueryService;
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
        Page<Post> posts = postRepository.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
        List<Long> postSeqList = getSeqList(posts);
        Page<PostResult> postResults = posts.map(Post::toResult);
        return this.assemblePostResult(postSeqList, postResults);
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            PageRequest pageRequest
    ) {
        Page<Post> posts = postRepository.getPostsByKeyword(keyword, pageRequest);
        List<Long> postSeqList = this.getSeqList(posts);
        Page<PostResult> postResults = posts.map(Post::toResult);
        postResults.getContent().forEach(post -> post.addKeywords(keyword));
        return this.assemblePostResult(postSeqList, postResults);
    }

    @Transactional(readOnly = true)
    public PostResult getPostDetail(Long postSeq) {
        Page<Post> posts = new PageImpl<>(List.of(postRepository.getById(postSeq)));
        Page<PostResult> postResult = posts.map(Post::toResult);
        return this.assemblePostResult(List.of(postSeq), postResult).getContent().getFirst();
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        Page<Post> posts = postRepository.getPostsByUserSeq(userSeq, pageable);
        Page<PostResult> postResults = posts.map(Post::toResult);
        return this.assemblePostResult(this.getSeqList(posts), postResults);
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

    private static List<Long> getSeqList(Page<Post> posts) {
        return posts.stream().map(Post::seq).toList();
    }

    private Page<PostResult> assemblePostResult(
            List<Long> resultSeqList,
            Page<PostResult> postResults
    ) {
        Map<Long, Integer> orderMap = IntStream.range(0, resultSeqList.size())
                .boxed()
                .collect(Collectors.toMap(resultSeqList::get, i -> i));

        List<InteractionUser> interactionUsers = interactionUserQueryService.getInteractionUsersByPostSeqList(
                resultSeqList,
                PostType.POST
        );
        List<PostResource> postResources = postResourceQueryService.getResourcesByPostSeqList(
                resultSeqList,
                PostType.POST
        );

        return postResults.map(postResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.postSeq().equals(postResult.getSeq()))
                    .sorted(Comparator.comparingInt(pr -> orderMap.getOrDefault(pr.postSeq(), Integer.MAX_VALUE)))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.postSeq().equals(postResult.getSeq()))
                    .sorted(Comparator.comparingInt(iu -> orderMap.getOrDefault(iu.postSeq(), Integer.MAX_VALUE)))
                    .toList();

            postResult.addInteractionUsers(interactionUserList);
            postResult.addResources(resources);
            postResult.updateLikeCount(interactionUserList.size());

            return postResult;
        });
    }


}
