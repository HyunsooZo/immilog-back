package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.services.CommentCommandService;
import com.backend.immilog.post.application.services.PostCommandService;
import com.backend.immilog.post.application.services.PostQueryService;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.presentation.request.CommentUploadRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.backend.immilog.post.exception.PostErrorCode.INVALID_REFERENCE_TYPE;
import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("CommentUploadService 테스트")
class CommentUploadUseCaseTest {
    private final CommentCommandService commentCommandService = mock(CommentCommandService.class);
    private final PostQueryService postQueryService = mock(PostQueryService.class);
    private final PostCommandService postCommandService = mock(PostCommandService.class);
    private final CommentUploadUseCase commentUploadUseCase = new CommentUploadUseCase.CommentUploader(
            commentCommandService,
            postQueryService,
            postCommandService
    );

    @Test
    @DisplayName("댓글 업로드 - 성공")
    void uploadComment() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        String referenceType = "post";
        CommentUploadRequest commentUploadRequest = new CommentUploadRequest("content");
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        // when
        commentUploadUseCase.uploadComment(
                userId,
                postSeq,
                referenceType,
                commentUploadRequest.content()
        );
        // then
        verify(postQueryService, times(1)).getPostById(postSeq);
        verify(commentCommandService, times(1)).save(any());
    }

    @Test
    @DisplayName("댓글 업로드 - 실패:없는 게시물")
    void uploadComment_fail() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        String referenceType = "posts";
        CommentUploadRequest commentUploadRequest =
                new CommentUploadRequest("content");
        when(postQueryService.getPostById(postSeq)).thenThrow(new PostException(POST_NOT_FOUND));
        // when & then
        assertThatThrownBy(() -> commentUploadUseCase.uploadComment(
                userId,
                postSeq,
                referenceType,
                commentUploadRequest.content()
        ))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 업로드 - 실패:없는 참조 타입")
    void uploadComment_fail_invalid_ref_type() {
        // given
        Long userId = 1L;
        Long postSeq = 1L;
        String referenceType = "text";
        CommentUploadRequest commentUploadRequest =
                new CommentUploadRequest("content");
        Post post = mock(Post.class);
        when(postQueryService.getPostById(postSeq)).thenReturn(post);
        // when & then
        assertThatThrownBy(() -> commentUploadUseCase.uploadComment(
                userId,
                postSeq,
                referenceType,
                commentUploadRequest.content()
        ))
                .isInstanceOf(PostException.class)
                .hasMessage(INVALID_REFERENCE_TYPE.getMessage());
    }

}