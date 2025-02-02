package com.backend.immilog.post.application.services;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.query.CommentQueryService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.SortingMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PostInquiryService {
    private final PostQueryService postQueryService;
    private final CommentQueryService commentQueryService;

    public PostInquiryService(
            PostQueryService postQueryService,
            CommentQueryService commentQueryService
    ) {
        this.postQueryService = postQueryService;
        this.commentQueryService = commentQueryService;
    }

    public Page<PostResult> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Integer page
    ) {
        Pageable pageable = PageRequest.of(Objects.requireNonNullElse(page, 0), 10);
        return postQueryService.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public PostResult getPostDetail(Long postSeq) {
        PostResult post = postQueryService.getPostDetail(postSeq);
        List<CommentResult> comments = commentQueryService.getComments(postSeq);
        post.addComments(comments);
        return post;
    }

    public Page<PostResult> searchKeyword(
            String keyword,
            Integer page
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<PostResult> posts = postQueryService.getPostsByKeyword(keyword, pageRequest);
        posts.getContent().forEach(post -> post.addKeywords(keyword));
        return posts;
    }

    public Page<PostResult> getUserPosts(
            Long userSeq,
            Integer page
    ) {
        Pageable pageable = PageRequest.of(Objects.requireNonNullElse(page, 0), 10);
        return postQueryService.getPostsByUserSeq(userSeq, pageable);
    }

    public List<PostResult> getMostViewedPosts() {
        return postQueryService.getPostsFromRedis("most_viewed_posts");
    }

    public List<PostResult> getHotPosts() {
        return postQueryService.getPostsFromRedis("hot_posts");
    }
}
