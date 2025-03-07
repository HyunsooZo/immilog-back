package com.backend.immilog.post.application.services.query;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.infrastructure.repositories.PostRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PostQueryService 테스트")
class PostQueryServiceTest {

    private final PostRepository postRepository = mock(PostRepositoryImpl.class);
    private final DataRepository redisDataRepository = mock(DataRepository.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final InteractionUserQueryService InteractionUserQueryService = mock(InteractionUserQueryService.class);
    private final PostResourceQueryService PostResourceQueryService = mock(PostResourceQueryService.class);
    private final PostQueryService postQueryService = new PostQueryService(
            objectMapper,
            postRepository,
            redisDataRepository,
            InteractionUserQueryService,
            PostResourceQueryService
    );

    @Test
    @DisplayName("getPostById 메서드가 Post를 성공적으로 반환")
    void getPostByIdReturnsPostSuccessfully() {
        Long postSeq = 1L;
        Post expectedPost = new Post(
                1L,
                null,
                null,
                Categories.QNA,
                "Y",
                Badge.HOT,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()

        );
        when(postRepository.getById(postSeq)).thenReturn(expectedPost);
        Post actualPost = postQueryService.getPostById(postSeq);
        assertThat(actualPost).isEqualTo(expectedPost);
    }

    @Test
    @DisplayName("getPostById 메서드가 빈 Optional을 반환")
    void getPostByIdReturnsEmptyOptional() {
        Long postSeq = 1L;
        when(postRepository.getById(postSeq)).thenThrow(new PostException(POST_NOT_FOUND));
        assertThatThrownBy(() -> postQueryService.getPostById(postSeq))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("getPosts 메서드가 PostResult 페이지를 성공적으로 반환")
    void getPostsReturnsPostResultsSuccessfully() {
        Country country = Country.SOUTH_KOREA;
        SortingMethods sortingMethod = SortingMethods.CREATED_DATE;
        String isPublic = "true";
        Categories category = Categories.COMMUNICATION;
        Pageable pageable = mock(Pageable.class);
        Page<PostResult> expectedPage = new PageImpl<>(List.of(new PostResult(
                1L,
                "title",
                "content",
                1L,
                "userProfileUrl",
                "userNickName",
                List.of(),
                0L,
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                "true",
                "SOUTH_KOREA",
                "region",
                Categories.COMMUNICATION,
                null,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                null
        )));
        when(postRepository.getPosts(country, sortingMethod, isPublic, category, pageable)).thenReturn(expectedPage.map(Post::from));

        Page<PostResult> actualPage = postQueryService.getPosts(country, sortingMethod, isPublic, category, pageable);

        assertThat(actualPage).isNotEmpty();
        assertThat(actualPage.get().findFirst().get().getSeq()).isEqualTo(expectedPage.get().findFirst().get().getSeq());
    }

    @Test
    @DisplayName("getPostsByKeyword 메서드가 PostResult 페이지를 성공적으로 반환")
    void getPostsByKeywordReturnsPostResultsSuccessfully() {
        String keyword = "test";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PostResult> expectedPage = new PageImpl<>(List.of(new PostResult(
                1L,
                "title",
                "content",
                1L,
                "userProfileUrl",
                "userNickName",
                List.of(),
                0L,
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                "true",
                "SOUTH_KOREA",
                "region",
                Categories.COMMUNICATION,
                null,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                null
        )));
        when(postRepository.getPostsByKeyword(keyword, pageRequest)).thenReturn(expectedPage.map(Post::from));

        Page<PostResult> actualPage = postQueryService.getPostsByKeyword(keyword, pageRequest);

        assertThat(actualPage).isNotEmpty();
        assertThat(actualPage.get().findFirst().get().getSeq()).isEqualTo(expectedPage.get().findFirst().get().getSeq());
    }

    @Test
    @DisplayName("getPost 메서드가 PostResult를 성공적으로 반환")
    void getPostReturnsPostResultSuccessfully() {
        Long postSeq = 1L;
        PostInfo postInfo = new PostInfo("title", "content", 0L, 0L, "region", null, Country.SOUTH_KOREA);
        PostUserInfo postUserInfo = new PostUserInfo(1L, "userProfileUrl", "userNickName");
        Post post = new Post(
                1L,
                postUserInfo,
                postInfo,
                Categories.COMMUNICATION,
                "true",
                Badge.HOT,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(postRepository.getById(postSeq)).thenReturn(post);

        PostResult actualPostResult = postQueryService.getPostDetail(postSeq);

        assertThat(actualPostResult.getSeq()).isEqualTo(post.seq());
    }

    @Test
    @DisplayName("getPostsByUserSeq 메서드가 PostResult 페이지를 성공적으로 반환")
    void getPostsByUserSeqReturnsPostResultsSuccessfully() {
        Long userSeq = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<PostResult> expectedPage = new PageImpl<>(List.of(new PostResult(
                1L,
                "title",
                "content",
                1L,
                "userProfileUrl",
                "userNickName",
                List.of(),
                0L,
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                "true",
                "SOUTH_KOREA",
                "region",
                Categories.COMMUNICATION,
                null,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                null
        )));
        when(postRepository.getPostsByUserSeq(userSeq, pageable)).thenReturn(expectedPage.map(Post::from));

        Page<PostResult> actualPage = postQueryService.getPostsByUserSeq(userSeq, pageable);

        assertThat(actualPage.get().findFirst().get().getSeq()).isEqualTo(expectedPage.get().findFirst().get().getSeq());
    }

    @Test
    @DisplayName("getPostsFromRedis 메서드가 Redis에서 PostResult 리스트를 성공적으로 반환")
    void getPostsFromRedisReturnsPostResultsSuccessfully() throws JsonProcessingException {
        String key = "testKey";
        String jsonData = "[{\"id\":1}]";
        List<PostResult> expectedPosts = List.of(new PostResult(
                1L,
                "title",
                "content",
                1L,
                "userProfileUrl",
                "userNickName",
                List.of(),
                0L,
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                "true",
                "SOUTH_KOREA",
                "region",
                Categories.COMMUNICATION,
                null,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                null
        ));
        when(redisDataRepository.findByKey(key)).thenReturn(jsonData);
        when(objectMapper.readValue(jsonData, new TypeReference<List<PostResult>>() {})).thenReturn(expectedPosts);

        List<PostResult> actualPosts = postQueryService.getPostsFromRedis(key);
    }

    @Test
    @DisplayName("getPostsFromRedis 메서드가 빈 리스트를 반환")
    void getPostsFromRedisReturnsEmptyList() {
        String key = "testKey";
        when(redisDataRepository.findByKey(key)).thenReturn(null);

        List<PostResult> actualPosts = postQueryService.getPostsFromRedis(key);

        assertThat(actualPosts).isEmpty();
    }
}