package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.query.CommentQueryService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.exception.PostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostInquiryService {
    private final PostQueryService postQueryService;
    private final CommentQueryService commentQueryService;

    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Integer page
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        return postQueryService.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public PostResult getPost(
            Long postSeq
    ) {
        PostResult postResult = getPostDTO(postSeq);
        List<CommentResult> comments = commentQueryService.getComments(postSeq);
        postResult = postResult.copyWithNewComments(comments);
        return postResult;
    }

    public Page<PostResult> searchKeyword(
            String keyword,
            Integer page
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        List<PostResult> postResults = postQueryService.getPostsByKeyword(keyword, pageRequest)
                .getContent()
                .stream()
                .map(postResult -> postResult.copyWithKeyword(keyword))
                .toList();
        return new PageImpl<>(postResults, pageRequest, postResults.size());
    }

    public Page<PostResult> getUserPosts(
            Long userSeq,
            Integer page
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        return postQueryService.getPostsByUserSeq(userSeq, pageable);
    }

    private PostResult getPostDTO(
            Long postSeq
    ) {
        return postQueryService
                .getPost(postSeq)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }
}
