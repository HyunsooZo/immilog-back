package com.backend.immilog.post.application;

import com.backend.immilog.post.application.services.PostDeleteService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import com.backend.immilog.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.backend.immilog.post.domain.enums.PostStatus.DELETED;
import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostDeleteService 테스트")
class PostDeleteServiceTest {
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final PostDeleteService postDeleteService = new PostDeleteService(
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
        Post post = Post.builder()
                .seq(postSeq)
                .postInfo(PostInfo.builder().build())
                .postUserInfo(PostUserInfo.builder().userSeq(userId).build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));

        // when
        postDeleteService.deletePost(userId, postSeq);

        // then
        verify(postQueryService).getPostById(postSeq);
        verify(postResourceCommandService).deleteAllByPostSeq(postSeq);
        assertThat(post.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @DisplayName("게시물 삭제 - 실패:권한없음")
    void deletePost_failed() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        Post post = Post.builder()
                .seq(postSeq)
                .postInfo(PostInfo.builder().build())
                .postUserInfo(PostUserInfo.builder().userSeq(2L).build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> postDeleteService.deletePost(userId, postSeq))
                .isInstanceOf(PostException.class)
                .hasMessage(NO_AUTHORITY.getMessage());

    }
}