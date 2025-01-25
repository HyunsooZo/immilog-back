package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.comment.CommentRelation;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("CommentCommandService 테스트")
class CommentCommandServiceTest {

    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final CommentCommandService commentCommandService = new CommentCommandService(commentRepository);

    @Test
    @DisplayName("save 메서드가 Comment를 성공적으로 저장")
    void saveSavesCommentSuccessfully() {
        Comment comment = new Comment(
                1L,
                1L,
                0,
                0,
                "content",
                CommentRelation.of(1L, 1L, ReferenceType.POST),
                PostStatus.NORMAL,
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        commentCommandService.save(comment);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());

        assertThat(commentCaptor.getValue()).isEqualTo(comment);
    }

    @Test
    @DisplayName("save 메서드가 null Comment를 처리")
    void saveHandlesNullComment() {
        Comment comment = null;

        commentCommandService.save(comment);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());

        assertThat(commentCaptor.getValue()).isNull();
    }
}