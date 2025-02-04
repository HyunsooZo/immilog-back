package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PopularPostRepository;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("PostCommandService 테스트")
class PostCommandServiceTest {

    private final PostRepository postRepository = mock(PostRepository.class);
    private final PopularPostRepository popularPostRepository = mock(PopularPostRepository.class);
    private final PostCommandService postCommandService = new PostCommandService(postRepository, popularPostRepository);

    @Test
    @DisplayName("save 메서드가 Post를 성공적으로 저장")
    void saveSavesPostSuccessfully() {
        Post post = new Post(
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

        postCommandService.save(post);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());

        assertThat(postCaptor.getValue()).isEqualTo(post);
    }

    @Test
    @DisplayName("save 메서드가 null Post를 처리")
    void saveHandlesNullPost() {
        Post post = null;

        postCommandService.save(post);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());

        assertThat(postCaptor.getValue()).isNull();
    }

    @Test
    @DisplayName("saveMostViewedPosts 메서드가 인기 게시물을 성공적으로 저장")
    void saveMostViewedPostsSavesPopularPostsSuccessfully() throws JsonProcessingException {
        List<PostResult> posts = List.of(new PostResult(
                1L,
                "title",
                "content",
                1L,
                "url",
                "nickname",
                null,
                0L,
                0L,
                0L,
                null,
                null,
                null,
                null,
                "Y",
                "country",
                "region",
                Categories.QNA,
                null,
                "2021-08-01T00:00:00",
                "2021-08-01T00:00:00",
                null
        ));
        Integer expiration = 3600;

        postCommandService.saveMostViewedPosts(posts, expiration);

        ArgumentCaptor<List> postsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> expirationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(popularPostRepository).saveMostViewedPosts(postsCaptor.capture(), expirationCaptor.capture());

        assertThat(postsCaptor.getValue()).isEqualTo(posts);
        assertThat(expirationCaptor.getValue()).isEqualTo(expiration);
    }

    @Test
    @DisplayName("saveMostViewedPosts 메서드가 빈 인기 게시물 리스트를 처리")
    void saveMostViewedPostsHandlesEmptyPopularPostsList() throws JsonProcessingException {
        List<PostResult> posts = List.of();
        Integer expiration = 3600;

        postCommandService.saveMostViewedPosts(posts, expiration);

        ArgumentCaptor<List> postsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> expirationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(popularPostRepository).saveMostViewedPosts(postsCaptor.capture(), expirationCaptor.capture());

        assertThat(postsCaptor.getValue()).isEqualTo(posts);
        assertThat(expirationCaptor.getValue()).isEqualTo(expiration);
    }

    @Test
    @DisplayName("saveHotPosts 메서드가 핫 게시물을 성공적으로 저장")
    void saveHotPostsSavesHotPostsSuccessfully() throws JsonProcessingException {
        List<PostResult> popularPosts = List.of(
                new PostResult(
                        1L,
                        "title",
                        "content",
                        1L,
                        "url",
                        "nickname",
                        null,
                        0L,
                        0L,
                        0L,
                        null,
                        null,
                        null,
                        null,
                        "Y",
                        "country",
                        "region",
                        Categories.QNA,
                        null,
                        "2021-08-01T00:00:00",
                        "2021-08-01T00:00:00",
                        null
                )
        );
        int expiration = 3600;

        postCommandService.saveHotPosts(popularPosts, expiration);

        ArgumentCaptor<List> popularPostsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> expirationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(popularPostRepository).saveHotPosts(popularPostsCaptor.capture(), expirationCaptor.capture());

        assertThat(popularPostsCaptor.getValue()).isEqualTo(popularPosts);
        assertThat(expirationCaptor.getValue()).isEqualTo(expiration);
    }

    @Test
    @DisplayName("saveHotPosts 메서드가 빈 핫 게시물 리스트를 처리")
    void saveHotPostsHandlesEmptyHotPostsList() throws JsonProcessingException {
        List<PostResult> popularPosts = List.of();
        int expiration = 3600;

        postCommandService.saveHotPosts(popularPosts, expiration);

        ArgumentCaptor<List> popularPostsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> expirationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(popularPostRepository).saveHotPosts(popularPostsCaptor.capture(), expirationCaptor.capture());

        assertThat(popularPostsCaptor.getValue()).isEqualTo(popularPosts);
        assertThat(expirationCaptor.getValue()).isEqualTo(expiration);
    }
}