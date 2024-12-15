package com.backend.immilog.post.application;

import com.backend.immilog.global.infrastructure.persistence.lock.RedisDistributedLock;
import com.backend.immilog.post.application.services.PostUpdateService;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.presentation.request.PostUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.backend.immilog.post.exception.PostErrorCode.FAILED_TO_SAVE_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostUpdateService 테스트")
class PostUpdateServiceTest {
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final BulkCommandService bulkCommandService = mock(BulkCommandService.class);
    private final RedisDistributedLock redisDistributedLock = mock(RedisDistributedLock.class);
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

    private final PostUpdateService postUpdateService = new PostUpdateService(
            postQueryService,
            postCommandService,
            postResourceCommandService,
            bulkCommandService,
            redisDistributedLock
    );

    @BeforeEach
    void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    @DisplayName("게시물 수정 - 성공")
    void updatePost() throws SQLException {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .addAttachments(List.of("attachment"))
                .deleteAttachments(List.of("delete-attachment"))
                .addTags(List.of("tag"))
                .deleteTags(List.of("delete-tag"))
                .title("title")
                .content("content")
                .isPublic(true)
                .build();
        Post post = Post.builder()
                .postUserInfo(PostUserInfo.builder().userSeq(1L).build())
                .postInfo(PostInfo.builder().build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));
        doNothing().when(preparedStatement).setLong(eq(1), anyLong());
        doNothing().when(preparedStatement).setString(eq(2), anyString());
        doNothing().when(preparedStatement).setString(eq(3), anyString());
        doNothing().when(preparedStatement).setString(eq(4), anyString());

        ArgumentCaptor<BiConsumer<PreparedStatement, PostResource>> captor =
                ArgumentCaptor.forClass(BiConsumer.class);

        // when
        postUpdateService.updatePost(userSeq, postSeq, postUpdateRequest.toCommand());

        // then
        verify(postResourceCommandService, times(2))
                .deleteAllEntities(anyLong(), any(PostType.class), any(ResourceType.class),
                        anyList());
        verify(bulkCommandService, times(2)).saveAll(
                anyList(),
                anyString(),
                captor.capture()
        );
    }

    @Disabled
    @Test
    @DisplayName("게시물 수정 - 실패")
    void updatePostFailed() throws SQLException {
        // given
        Long userSeq = 1L;
        Long postSeq = 1L;
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .addAttachments(List.of("attachment"))
                .deleteAttachments(List.of("delete-attachment"))
                .addTags(List.of("tag"))
                .deleteTags(List.of("delete-tag"))
                .title("title")
                .content("content")
                .isPublic(true)
                .build();
        Post post = Post.builder()
                .postUserInfo(PostUserInfo.builder().userSeq(1L).build())
                .postInfo(PostInfo.builder().build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));

        doThrow(new SQLException("Mock SQL Exception"))
                .when(preparedStatement).setLong(anyInt(), anyLong());

        doAnswer(invocation -> {
            BiConsumer<PreparedStatement, PostResource> consumer = invocation.getArgument(2);
            PostResource postResource = PostResource.builder().postSeq(1L).build();

            consumer.accept(preparedStatement, postResource);

            return null;
        }).when(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));

        // when & then
        assertThatThrownBy(() ->
                postUpdateService.updatePost(userSeq, postSeq,
                        postUpdateRequest.toCommand()))
                .isInstanceOf(PostException.class)
                .hasMessage(FAILED_TO_SAVE_POST.getMessage());

        verify(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));

    }

    @Test
    @DisplayName("게시물 조회수 증가 - 성공")
    void increaseViewCount() {
        // given
        Long postSeq = 1L;
        Post post = Post.builder()
                .postInfo(PostInfo.builder().viewCount(0L).build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));
        when(redisDistributedLock.tryAcquireLock(anyString(), anyString()))
                .thenReturn(true);
        // when
        postUpdateService.increaseViewCount(postSeq);

        // then
        assertThat(post.getViewCount()).isEqualTo(1L);
        verify(redisDistributedLock).releaseLock(anyString(), anyString());
    }

    @Test
    @DisplayName("게시물 조회수 증가 - 실패")
    void increaseViewCount_fail() {
        // given
        Long postSeq = 1L;
        Post post = Post.builder()
                .postInfo(PostInfo.builder().viewCount(0L).build())
                .build();
        when(postQueryService.getPostById(postSeq)).thenReturn(Optional.of(post));
        when(redisDistributedLock.tryAcquireLock(anyString(), anyString()))
                .thenReturn(false, false, false);
        // when
        postUpdateService.increaseViewCount(postSeq);

        // then
        assertThat(post.getViewCount()).isEqualTo(0L);
    }

}