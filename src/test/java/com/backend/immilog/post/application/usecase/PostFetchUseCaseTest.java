package com.backend.immilog.post.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.mapper.PostResultAssembler;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.CommentQueryService;
import com.backend.immilog.post.application.services.InteractionUserQueryService;
import com.backend.immilog.post.application.services.PostQueryService;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.PostStatus;
import com.backend.immilog.post.domain.model.post.SortingMethods;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PostInquiryService 테스트")
class PostFetchUseCaseTest {
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final CommentQueryService commentQueryService = mock(CommentQueryService.class);
    private final InteractionUserQueryService interactionUserQueryService = mock(InteractionUserQueryService.class);
    private final PostResultAssembler postResultAssembler = new PostResultAssembler();
    private final PostFetchUseCase postFetchUseCase = new PostFetchUseCase.PostFetcher(
            postQueryService,
            commentQueryService,
            interactionUserQueryService,
            postResultAssembler
    );

    @Test
    @DisplayName("게시물 조회 - 성공")
    void getPostsDetail() {
        // given
        var country = Country.SOUTH_KOREA;
        var sortingMethod = SortingMethods.CREATED_DATE;
        var isPublic = "Y";
        var category = Categories.ALL;
        int page = 0;
        var pageable = PageRequest.of(page, 10);
        var postResult = mock(PostResult.class);
        var posts = new PageImpl<PostResult>(List.of(postResult));
        when(postQueryService.getPosts(country, sortingMethod, isPublic, category, pageable)).thenReturn(posts);
        // when
        var result = postFetchUseCase.getPosts(country, sortingMethod, isPublic, category, page);
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시물 조회(단일 게시물)")
    void getPostDetail() {
        // given
        var postSeq = 1L;
        var postResult = mock(PostResult.class);
        when(postQueryService.getPostDetail(postSeq)).thenReturn(postResult);
        when(commentQueryService.getComments(postSeq)).thenReturn(List.of());
        // when
        PostResult result = postFetchUseCase.getPostDetail(postSeq);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("게시물 검색 - 성공")
    void searchKeyword() {
        // given
        var keyword = "keyword";
        int page = 0;
        var pageable = PageRequest.of(page, 10);
        var postResult = new PostResult(
                1L,
                1L,
                "profileUrl",
                "nickName",
                new ArrayList<>(),
                0L,
                0L,
                0L,
                Arrays.asList("tag1", "tag2"),
                Arrays.asList("attachment1", "attachment2"),
                new ArrayList<>(),
                new ArrayList<>(),
                "Y",
                Country.SOUTH_KOREA.name(),
                "region",
                Categories.ALL,
                PostStatus.NORMAL,
                "2023-10-01T00:00:00",
                "2023-10-01T00:00:00",
                "title",
                "content",
                keyword
        );
        var posts = new PageImpl<PostResult>(List.of(postResult));
        when(postQueryService.getPostsByKeyword(keyword, pageable)).thenReturn(posts);
        // when
        var result = postFetchUseCase.searchKeyword(keyword, page);
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 게시물 조회 - 성공")
    void getUserPosts() {
        // given
        var userSeq = 1L;
        int page = 0;
        var pageable = PageRequest.of(page, 10);
        var postResult = mock(PostResult.class);
        var posts = new PageImpl<PostResult>(List.of(postResult));
        when(postQueryService.getPostsByUserSeq(userSeq, pageable)).thenReturn(posts);
        // when
        var result = postFetchUseCase.getUserPosts(userSeq, page);
        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

}