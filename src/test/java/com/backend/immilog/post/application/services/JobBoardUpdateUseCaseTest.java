package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.usecase.JobBoardUpdateUseCase;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.service.JobBoardValidator;
import com.backend.immilog.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("JobBoardUpdateService 테스트")
class JobBoardUpdateUseCaseTest {

    private final JobBoardQueryService jobBoardQueryService = mock(JobBoardQueryService.class);
    private final JobBoardCommandService jobBoardCommandService = mock(JobBoardCommandService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final BulkCommandService bulkInsertRepository = mock(BulkCommandService.class);
    private final JobBoardValidator jobBoardValidator =new JobBoardValidator();
    private final JobBoardUpdateUseCase jobBoardUpdateUseCase = new JobBoardUpdateUseCase.JobBoardUpdater(
            jobBoardQueryService,
            jobBoardCommandService,
            postResourceCommandService,
            bulkInsertRepository,
            jobBoardValidator
    );

    @Test
    @DisplayName("구인구직 업데이트 - 성공")
    void updateJobBoard_updatesTagsAndAttachments() {
        LocalDateTime now = LocalDateTime.now();
        ArgumentCaptor<BiConsumer<PreparedStatement, PostResource>> captor = ArgumentCaptor.forClass(BiConsumer.class);
        JobBoardUpdateCommand command = new JobBoardUpdateCommand(
                "New Title",
                "New Content",
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3"),
                now,
                Experience.JUNIOR,
                "1000"
        );
        JobBoardResult jobBoard = new JobBoardResult(
                1L,
                "Title",
                "Content",
                0L,
                0L,
                List.of("tag1", "tag3"),
                List.of("att1", "att3"),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                null,
                null
        );
        when(jobBoardQueryService.getJobBoardBySeq(anyLong())).thenReturn(jobBoard);
        jobBoardUpdateUseCase.updateJobBoard(1L, 1L, command);
        verify(postResourceCommandService).deleteAllEntities(anyLong(), eq(PostType.JOB_BOARD), eq(ResourceType.TAG), anyList());
        verify(postResourceCommandService).deleteAllEntities(anyLong(), eq(PostType.JOB_BOARD), eq(ResourceType.ATTACHMENT), anyList());
        verify(bulkInsertRepository, times(2)).saveAll(anyList(), anyString(), any());
        verify(bulkInsertRepository, times(2)).saveAll(
                anyList(),
                anyString(),
                captor.capture()
        );
    }

    @Test
    @DisplayName("구인구직 업데이트 - 실패 : 매니저 아닌 경우")
    void updateJobBoard_throwsExceptionIfUserIsNotOwner() {
        LocalDateTime now = LocalDateTime.now();
        //public record JobBoardUpdateCommand(
        //        String title,
        //        String content,
        //        List<String> deleteTags,
        //        List<String> addTags,
        //        List<String> deleteAttachments,
        //        List<String> addAttachments,
        //        LocalDateTime deadline,
        //        Experience experience,
        //        String salary
        //) {
        JobBoardUpdateCommand command = new JobBoardUpdateCommand(
                "New Title",
                "New Content",
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3"),
                now,
                Experience.JUNIOR,
                "1000"
        );
        JobBoardResult jobBoard = new JobBoardResult(
                1L,
                "Title",
                "Content",
                0L,
                0L,
                List.of("tag1", "tag3"),
                List.of("att1", "att3"),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                3L,
                null,
                null
        );
        when(jobBoardQueryService.getJobBoardBySeq(anyLong())).thenReturn(jobBoard);

        assertThatThrownBy(() -> jobBoardUpdateUseCase.updateJobBoard(1L, 1L, command))
                .isInstanceOf(PostException.class)
                .hasMessage(NO_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("구인구직 게시글 삭제 - 성공")
    void deleteJobBoard_deletesJobBoard() {
        JobBoardResult jobBoard = new JobBoardResult(
                1L,
                "Title",
                "Content",
                0L,
                0L,
                List.of("tag1", "tag3"),
                List.of("att1", "att3"),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                null,
                null
        );
        when(jobBoardQueryService.getJobBoardBySeq(anyLong())).thenReturn(jobBoard);
        jobBoardUpdateUseCase.deactivateJobBoard(1L, 1L);
        verify(jobBoardCommandService).save(any());
    }
}