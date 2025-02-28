package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.PostUpdateCommand;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("PostUpdateService 테스트")
class PostUpdateServiceTest {

    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final BulkCommandService bulkCommandService = mock(BulkCommandService.class);
    private final PostUpdateService postUpdateService = new PostUpdateService(
            postQueryService,
            postCommandService,
            postResourceCommandService,
            bulkCommandService
    );

    @Test
    @DisplayName("게시물 업데이트 - 성공")
    void updatePost_success() {
        Long userId = 1L;
        Long postSeq = 1L;
        PostUpdateCommand command = new PostUpdateCommand(
                "New Title",
                "New Content",
                true,
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3")
        );
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.userSeq()).thenReturn(userId);
        when(post.updateTitle(command.title())).thenReturn(post);
        when(post.updateContent(command.content())).thenReturn(post);
        when(post.updateIsPublic(command.isPublic())).thenReturn(post);

        postUpdateService.updatePost(userId, postSeq, command);

        verify(postCommandService).save(post);
        verify(postResourceCommandService).deleteAllEntities(postSeq, PostType.POST, ResourceType.TAG, command.deleteTags());
        verify(postResourceCommandService).deleteAllEntities(postSeq, PostType.POST, ResourceType.ATTACHMENT, command.deleteAttachments());
        verify(bulkCommandService).saveAll(command.addTags(), anyString(), any());
        verify(bulkCommandService).saveAll(command.addAttachments(), anyString(), any());
    }

    @Test
    @DisplayName("게시물 업데이트 - 실패: 권한 없음")
    void updatePost_noAuthority() {
        Long userId = 1L;
        Long postSeq = 1L;
        PostUpdateCommand command = new PostUpdateCommand(
                "New Title",
                "New Content",
                true,
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3")
        );
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.userSeq()).thenReturn(2L);

        assertThatThrownBy(() -> postUpdateService.updatePost(userId, postSeq, command))
                .isInstanceOf(PostException.class)
                .hasMessage(PostErrorCode.NO_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("게시물 조회수 증가 - 성공")
    void increaseViewCount_success() {
        Long postSeq = 1L;
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.increaseViewCount()).thenReturn(post);

        postUpdateService.increaseViewCount(postSeq);

        verify(postCommandService).save(post);
    }

    @Test
    @DisplayName("게시물 업데이트 - 도메인 언어 및 애그리게이트 테스트")
    void updatePost_domainLanguageAndAggregate() {
        Long userId = 1L;
        Long postSeq = 1L;
        PostUpdateCommand command = new PostUpdateCommand(
                "New Title",
                "New Content",
                true,
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3")
        );
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.userSeq()).thenReturn(userId);
        when(post.updateTitle(command.title())).thenReturn(post);
        when(post.updateContent(command.content())).thenReturn(post);
        when(post.updateIsPublic(command.isPublic())).thenReturn(post);

        postUpdateService.updatePost(userId, postSeq, command);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postCommandService).save(postCaptor.capture());
        Post updatedPost = postCaptor.getValue();
        assertThat(updatedPost.getTitle()).isEqualTo("New Title");
        assertThat(updatedPost.getContent()).isEqualTo("New Content");
        assertThat(updatedPost.isPublic()).isEqualTo(true);
    }

    @Test
    @DisplayName("게시물 업데이트 - 새로운 테스트 추가")
    void updatePost_newTest() {
        Long userId = 1L;
        Long postSeq = 1L;
        PostUpdateCommand command = new PostUpdateCommand(
                "Updated Title",
                "Updated Content",
                false,
                List.of("tag4", "tag5"),
                List.of("att4", "att5"),
                List.of("tag6"),
                List.of("att6")
        );
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.userSeq()).thenReturn(userId);
        when(post.updateTitle(command.title())).thenReturn(post);
        when(post.updateContent(command.content())).thenReturn(post);
        when(post.updateIsPublic(command.isPublic())).thenReturn(post);

        postUpdateService.updatePost(userId, postSeq, command);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postCommandService).save(postCaptor.capture());
        Post updatedPost = postCaptor.getValue();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
        assertThat(updatedPost.isPublic()).isEqualTo(false);
    }

    @Test
    @DisplayName("게시물 업데이트 - 새로운 테스트 추가")
    void updatePost_newTest() {
        Long userId = 1L;
        Long postSeq = 1L;
        PostUpdateCommand command = new PostUpdateCommand(
                "Updated Title",
                "Updated Content",
                false,
                List.of("tag4", "tag5"),
                List.of("att4", "att5"),
                List.of("tag6"),
                List.of("att6")
        );
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        when(post.userSeq()).thenReturn(userId);
        when(post.updateTitle(command.title())).thenReturn(post);
        when(post.updateContent(command.content())).thenReturn(post);
        when(post.updateIsPublic(command.isPublic())).thenReturn(post);

        postUpdateService.updatePost(userId, postSeq, command);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postCommandService).save(postCaptor.capture());
        Post updatedPost = postCaptor.getValue();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
        assertThat(updatedPost.isPublic()).isEqualTo(false);
    }
}
