package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CommentQueryService 테스트")
class CommentQueryServiceTest {

    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final CommentQueryService commentQueryService = new CommentQueryService(commentRepository);

    @Test
    @DisplayName("getComments 메서드가 댓글을 성공적으로 반환")
    void getCommentsReturnsCommentsSuccessfully() {
        Long postSeq = 1L;
        List<CommentResult> expectedComments = List.of(new CommentResult(
                1L,
                null,
                "content",
                List.of(),
                0,
                0,
                0,
                List.of(),
                null,
                null
        ));
        when(commentRepository.getComments(postSeq)).thenReturn(expectedComments);

        List<CommentResult> actualComments = commentQueryService.getComments(postSeq);

        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("getComments 메서드가 빈 댓글 리스트를 반환")
    void getCommentsReturnsEmptyList() {
        Long postSeq = 1L;
        List<CommentResult> expectedComments = List.of();
        when(commentRepository.getComments(postSeq)).thenReturn(expectedComments);

        List<CommentResult> actualComments = commentQueryService.getComments(postSeq);

        assertThat(actualComments).isEqualTo(expectedComments);
    }
}