package com.backend.immilog.post.application;

import com.backend.immilog.post.application.services.PostUploadService;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.presentation.request.PostUploadRequest;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
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

import static com.backend.immilog.post.domain.enums.PostType.POST;
import static com.backend.immilog.post.domain.enums.ResourceType.ATTACHMENT;
import static com.backend.immilog.post.exception.PostErrorCode.FAILED_TO_SAVE_POST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostUploadService 테스트")
class PostUploadServiceTest {
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final BulkCommandService bulkCommandService = mock(BulkCommandService.class);
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

    private final PostUploadService postUploadService = new PostUploadService(
            postCommandService,
            userQueryService,
            bulkCommandService
    );

    @BeforeEach
    void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    @DisplayName("게시물 업로드 - 성공")
    void uploadPost() throws Exception {
        // given
        Long userSeq = 1L;
        PostUploadRequest postUploadRequest = PostUploadRequest.builder()
                .title("title")
                .attachments(List.of("attachment"))
                .tags(List.of("tag"))
                .isPublic(true)
                .category(Categories.COMMUNICATION)
                .content("content")
                .build();
        Location location = Location.of(UserCountry.SOUTH_KOREA, "region");
        User user = User.builder()
                .seq(userSeq)
                .location(location)
                .profile(Profile.of("test", "image.png", UserCountry.SOUTH_KOREA))
                .build();
        Post post = Post.builder().seq(1L).build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(postCommandService.save(any(Post.class))).thenReturn(post);

        doNothing().when(preparedStatement).setLong(eq(1), anyLong());
        doNothing().when(preparedStatement).setString(eq(2), anyString());
        doNothing().when(preparedStatement).setString(eq(3), anyString());
        doNothing().when(preparedStatement).setString(eq(4), anyString());

        ArgumentCaptor<BiConsumer<PreparedStatement, PostResource>> captor = ArgumentCaptor.forClass(BiConsumer.class);

        // when
        postUploadService.uploadPost(userSeq, postUploadRequest.toCommand());

        // then
        verify(postCommandService).save(any(Post.class));
        verify(bulkCommandService).saveAll(
                anyList(),
                anyString(),
                captor.capture()
        );
        PostResource capturedPostResource = PostResource.builder()
                .seq(1L)
                .postSeq(1L)
                .postType(POST)
                .resourceType(ATTACHMENT)
                .content("attachment")
                .build();
        captor.getValue().accept(preparedStatement, capturedPostResource);
        verify(preparedStatement).setLong(1, capturedPostResource.getPostSeq());
        verify(preparedStatement).setString(2, capturedPostResource.getPostType().name());
        verify(preparedStatement).setString(3, capturedPostResource.getResourceType().name());
        verify(preparedStatement).setString(4, capturedPostResource.getContent());
    }

    @Test
    @DisplayName("게시물 업로드 - 성공 (태그/첨부파일 없음)")
    void uploadPost_wo_tags_and_attachments() {
        // given
        Long userSeq = 1L;
        PostUploadRequest postUploadRequest = PostUploadRequest.builder()
                .title("title")
                .isPublic(true)
                .category(Categories.COMMUNICATION)
                .content("content")
                .build();
        Location location = Location.of(UserCountry.SOUTH_KOREA, "region");
        User user = User.builder()
                .seq(userSeq)
                .location(location)
                .profile(Profile.of("test", "image.png", UserCountry.SOUTH_KOREA))
                .build();
        Post post = Post.builder().seq(1L).build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(postCommandService.save(any(Post.class))).thenReturn(post);

        // when
        postUploadService.uploadPost(userSeq, postUploadRequest.toCommand());

        // then
        verify(postCommandService).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 업로드 - 실패 (예외 발생)")
    void uploadPost_throwsException() throws Exception {
        // given
        Long userSeq = 1L;
        PostUploadRequest postUploadRequest = PostUploadRequest.builder()
                .title("title")
                .attachments(List.of("attachment"))
                .tags(List.of("tag"))
                .isPublic(true)
                .category(Categories.COMMUNICATION)
                .content("content")
                .build();
        Location location = Location.of(UserCountry.SOUTH_KOREA, "region");
        User user = User.builder()
                .seq(userSeq)
                .location(location)
                .profile(Profile.of("test", "image.png", UserCountry.SOUTH_KOREA))
                .build();
        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(postCommandService.save(any(Post.class))).thenReturn(Post.builder().seq(1L).build());
        doThrow(new SQLException("Mock SQL Exception"))
                .when(preparedStatement).setLong(anyInt(), anyLong());

        doAnswer(invocation -> {
            BiConsumer<PreparedStatement, PostResource> consumer = invocation.getArgument(2);
            consumer.accept(preparedStatement, PostResource.builder().postSeq(1L).build());
            return null;
        }).when(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));

        // when & then
        assertThatThrownBy(() -> postUploadService.uploadPost(userSeq, postUploadRequest.toCommand()))
                .isInstanceOf(PostException.class)
                .hasMessage(FAILED_TO_SAVE_POST.getMessage());

        verify(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));
    }
}
