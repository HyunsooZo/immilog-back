package com.backend.immilog.post.application;

import com.backend.immilog.post.application.services.PostUpdateService;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.presentation.request.PostUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("PostUpdateService 테스트")
class PostUpdateServiceTest {
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final BulkCommandService bulkCommandService = mock(BulkCommandService.class);
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

    private final PostUpdateService postUpdateService = new PostUpdateService(
            postQueryService,
            postCommandService,
            postResourceCommandService,
            bulkCommandService
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
        //public record PostUpdateRequest(
        //        String title,
        //        String content,
        //        List<String> deleteTags,
        //        List<String> addTags,
        //        List<String> deleteAttachments,
        //        List<String> addAttachments,
        //        Boolean isPublic
        //) {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "title",
                "content",
                List.of("delete-tag"),
                List.of("tag"),
                List.of("delete-attachment"),
                List.of("attachment"),
                true
        );
        Post post = mock(Post.class);
        when(post.userSeq()).thenReturn(userSeq);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        doNothing().when(preparedStatement).setLong(eq(1), anyLong());
        doNothing().when(preparedStatement).setString(eq(2), anyString());
        doNothing().when(preparedStatement).setString(eq(3), anyString());
        doNothing().when(preparedStatement).setString(eq(4), anyString());
        Post post1 = mock(Post.class);
        Post post2 = mock(Post.class);
        Post post3 = mock(Post.class);
        when(post.updateTitle(anyString())).thenReturn(post1);
        when(post1.updateContent(anyString())).thenReturn(post2);
        when(post2.updateIsPublic(anyBoolean())).thenReturn(post3);

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

    @Test
    @DisplayName("게시물 조회수 증가 - 성공")
    void increaseViewCount() {
        // given
        Long postSeq = 1L;
        Post post = mock(Post.class);
        Post post1 = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.increaseViewCount()).thenReturn(post1);
        // when
        postUpdateService.increaseViewCount(postSeq);

        // then
        verify(postCommandService,times(1)).save(post1);
    }
}