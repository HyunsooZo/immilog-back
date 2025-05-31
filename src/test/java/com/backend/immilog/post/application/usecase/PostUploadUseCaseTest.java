package com.backend.immilog.post.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.services.BulkCommandService;
import com.backend.immilog.post.application.services.PostCommandService;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.presentation.request.PostUploadRequest;
import com.backend.immilog.user.application.services.UserQueryService;
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
import java.util.function.BiConsumer;

import static com.backend.immilog.post.domain.model.post.PostType.POST;
import static com.backend.immilog.post.domain.model.resource.ResourceType.ATTACHMENT;
import static com.backend.immilog.post.exception.PostErrorCode.FAILED_TO_SAVE_POST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostUploadService 테스트")
class PostUploadUseCaseTest {
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final BulkCommandService bulkCommandService = mock(BulkCommandService.class);
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

    private final PostUploadUseCase postUploadUseCase = new PostUploadUseCase.PostUploader(
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
        PostUploadRequest postUploadRequest =
                //public record PostUploadRequest(
                //        @NotBlank(message = "제목을 입력해주세요.") String title,
                //        @NotBlank(message = "내용을 입력해주세요.") String content,
                //        List<String> tags,
                //        List<String> attachments,
                //        @NotNull(message = "전체공개 여부를 입력해주세요.") Boolean isPublic,
                //        @NotNull(message = "카테고리를 입력해주세요.") Categories category
                //) {
                new PostUploadRequest(
                        "title",
                        "content",
                        List.of("tag"),
                        List.of("attachment"),
                        true,
                        Categories.COMMUNICATION
                );
        Location location = Location.of(Country.SOUTH_KOREA, "region");
        User user = new User(
                1L,
                null,
                null,
                null,
                Profile.of("test", null, null),
                location,
                null,
                null
        );

        Post post = new Post(
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        when(postCommandService.save(any(Post.class))).thenReturn(post);

        doNothing().when(preparedStatement).setLong(eq(1), anyLong());
        doNothing().when(preparedStatement).setString(eq(2), anyString());
        doNothing().when(preparedStatement).setString(eq(3), anyString());
        doNothing().when(preparedStatement).setString(eq(4), anyString());

        ArgumentCaptor<BiConsumer<PreparedStatement, PostResource>> captor = ArgumentCaptor.forClass(BiConsumer.class);

        // when
        postUploadUseCase.uploadPost(userSeq, postUploadRequest.toCommand());

        // then
        verify(postCommandService).save(any(Post.class));
        verify(bulkCommandService).saveAll(
                anyList(),
                anyString(),
                captor.capture()
        );
        PostResource capturedPostResource = new PostResource(
                1L,
                1L,
                POST,
                ATTACHMENT,
                "attachment"
        );
        captor.getValue().accept(preparedStatement, capturedPostResource);
        verify(preparedStatement).setLong(1, capturedPostResource.postSeq());
        verify(preparedStatement).setString(2, capturedPostResource.postType().name());
        verify(preparedStatement).setString(3, capturedPostResource.resourceType().name());
        verify(preparedStatement).setString(4, capturedPostResource.content());
    }

    @Test
    @DisplayName("게시물 업로드 - 성공 (태그/첨부파일 없음)")
    void uploadPost_wo_tags_and_attachments() {
        // given
        Long userSeq = 1L;
        PostUploadRequest postUploadRequest = new PostUploadRequest(
                "title",
                "content",
                null,
                null,
                true,
                Categories.COMMUNICATION
        );
        Location location = Location.of(Country.SOUTH_KOREA, "region");
        User user = new User(
                1L,
                null,
                null,
                null,
                Profile.of("test", null, null),
                location,
                null,
                null
        );
        Post post = new Post(
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        when(postCommandService.save(any(Post.class))).thenReturn(post);

        // when
        postUploadUseCase.uploadPost(userSeq, postUploadRequest.toCommand());

        // then
        verify(postCommandService).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 업로드 - 실패 (예외 발생)")
    void uploadPost_throwsException() throws Exception {
        // given
        Long userSeq = 1L;
        PostUploadRequest postUploadRequest = new PostUploadRequest(
                "title",
                "content",
                List.of("tag"),
                List.of("attachment"),
                true,
                Categories.COMMUNICATION
        );
        var location = Location.of(Country.SOUTH_KOREA, "region");
        var user = new User(
                1L,
                null,
                null,
                null,
                Profile.of("test", null, null),
                location,
                null,
                null
        );
        var post = new Post(1L, null, null, null, null, null,null, null,null);
        var postResource = new PostResource(1L, 1L, POST, ATTACHMENT, "attachment");

        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        when(postCommandService.save(any(Post.class))).thenReturn(post);
        doThrow(new SQLException("Mock SQL Exception")).when(preparedStatement).setLong(anyInt(), anyLong());

        doAnswer(invocation -> {
            BiConsumer<PreparedStatement, PostResource> consumer = invocation.getArgument(2);
            consumer.accept(preparedStatement, postResource);
            return null;
        }).when(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));

        // when & then
        assertThatThrownBy(() -> postUploadUseCase.uploadPost(userSeq, postUploadRequest.toCommand()))
                .isInstanceOf(PostException.class)
                .hasMessage(FAILED_TO_SAVE_POST.getMessage());

        verify(bulkCommandService).saveAll(anyList(), anyString(), any(BiConsumer.class));
    }
}
