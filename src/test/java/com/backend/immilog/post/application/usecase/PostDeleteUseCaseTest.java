package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.services.PostCommandService;
import com.backend.immilog.post.application.services.PostResourceCommandService;
import com.backend.immilog.post.application.services.PostQueryService;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import com.backend.immilog.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.backend.immilog.post.domain.model.post.PostStatus.DELETED;
import static com.backend.immilog.post.domain.model.post.PostStatus.NORMAL;
import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostDeleteService 테스트")
class PostDeleteUseCaseTest {
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final PostDeleteUseCase postDeleteUseCase = new PostDeleteUseCase.PostDeleter(
            postCommandService,
            postQueryService,
            postResourceCommandService
    );

    @Test
    @DisplayName("게시물 삭제 - 성공")
    void deletePost() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        Post post = new Post(
                postSeq,
                new PostUserInfo(userId, null, null),
                new PostInfo(null,null,null,null, null, NORMAL, null),
                Categories.ALL,
                "Y",
                null,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Post post2 = new Post(
                postSeq,
                new PostUserInfo(userId, null, null),
                new PostInfo(null,null,null,null, null, DELETED, null),
                Categories.ALL,
                "Y",
                null,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(postCommandService.save(any(Post.class))).thenReturn(post2);

        // when
        postDeleteUseCase.deletePost(userId, postSeq);

        // then
        verify(postQueryService).getPostById(postSeq);
        verify(postResourceCommandService).deleteAllByPostSeq(postSeq);
        assertThat(post2.status()).isEqualTo(DELETED);
    }

    @Test
    @DisplayName("게시물 삭제 - 실패:권한없음")
    void deletePost_failed() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        Post post = new Post(
                postSeq,
                new PostUserInfo(2L, null, null),
                new PostInfo(null,null,null,null, null, null, null),
                Categories.ALL,
                "Y",
                null,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(postQueryService.getPostById(postSeq)).thenReturn(post);

        // when & then
        assertThatThrownBy(() -> postDeleteUseCase.deletePost(userId, postSeq))
                .isInstanceOf(PostException.class)
                .hasMessage(NO_AUTHORITY.getMessage());

    }
}