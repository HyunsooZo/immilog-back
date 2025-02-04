package com.backend.immilog.post.application;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.PostInquiryService;
import com.backend.immilog.post.application.services.query.CommentQueryService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.SortingMethods;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PostInquiryService 테스트")
class PostInquiryServiceTest {
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final CommentQueryService commentQueryService = mock(CommentQueryService.class);
    private final PostInquiryService postInquiryService = new PostInquiryService(
            postQueryService,
            commentQueryService
    );

    @Test
    @DisplayName("게시물 조회 - 성공")
    void getPostsDetail() {
        // given
        Country country = Country.SOUTH_KOREA;
        SortingMethods sortingMethod = SortingMethods.CREATED_DATE;
        String isPublic = "Y";
        Categories category = Categories.ALL;
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        PostResult postResult = mock(PostResult.class);
        Page<PostResult> posts = new PageImpl<>(List.of(postResult));
        when(postQueryService.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        )).thenReturn(posts);
        // when
        Page<PostResult> result = postInquiryService.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                page
        );
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시물 조회(단일 게시물)")
    void getPostDetail() {
        // given
        Long postSeq = 1L;
        PostResult postResult = mock(PostResult.class);
        when(postQueryService.getPostDetail(postSeq)).thenReturn(postResult);
        when(commentQueryService.getComments(postSeq)).thenReturn(List.of());
        // when
        PostResult result = postInquiryService.getPostDetail(postSeq);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("게시물 검색 - 성공")
    void searchKeyword() {
        // given
        String keyword = "keyword";
        int page = 0;
        PageRequest pageable = PageRequest.of(page, 10);
        PostResult postResult = new PostResult(
                1L,
                "title",
                "content",
                1L,
                "url",
                "nickname",
                new ArrayList<>(),
                1L,
                1L,
                1L,
                new ArrayList<>(Arrays.asList("tag1", "tag2")),
                new ArrayList<>(Arrays.asList("attachment1", "attachment2")),
                new ArrayList<>(Arrays.asList(1L, 2L)),
                new ArrayList<>(Arrays.asList(1L, 2L)),
                "Y",
                "country",
                "region",
                Categories.ALL,
                PostStatus.NORMAL,
                "2021-01-01",
                "2021-01-01",
                "keyword"
        );
        Page<PostResult> posts = new PageImpl<>(List.of(postResult));
        when(postQueryService.getPostsByKeyword(keyword, pageable)).thenReturn(posts);
        // when
        Page<PostResult> result = postInquiryService.searchKeyword(keyword, page);
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 게시물 조회 - 성공")
    void getUserPosts() {
        // given
        Long userSeq = 1L;
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        PostResult postResult = mock(PostResult.class);
        Page<PostResult> posts = new PageImpl<>(List.of(postResult));
        when(postQueryService.getPostsByUserSeq(userSeq, pageable)).thenReturn(posts);
        // when
        Page<PostResult> result = postInquiryService.getUserPosts(userSeq, page);
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

}