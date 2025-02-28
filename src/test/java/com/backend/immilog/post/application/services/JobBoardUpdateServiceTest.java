package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.JobBoardCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.JobBoardQueryService;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("JobBoardUpdateService 테스트")
class JobBoardUpdateServiceTest {

    private final JobBoardQueryService jobBoardQueryService = mock(JobBoardQueryService.class);
    private final JobBoardCommandService jobBoardCommandService = mock(JobBoardCommandService.class);
    private final PostResourceCommandService postResourceCommandService = mock(PostResourceCommandService.class);
    private final BulkCommandService bulkInsertRepository = mock(BulkCommandService.class);
    private final JobBoardUpdateService jobBoardUpdateService = new JobBoardUpdateService(
            jobBoardQueryService,
            jobBoardCommandService,
            postResourceCommandService,
            bulkInsertRepository
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
        jobBoardUpdateService.updateJobBoard(1L, 1L, command);
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

        assertThatThrownBy(() -> jobBoardUpdateService.updateJobBoard(1L, 1L, command))
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
        jobBoardUpdateService.deactivateJobBoard(1L, 1L);
        verify(jobBoardCommandService).save(any());
    }

    @Test
    @DisplayName("구인구직 업데이트 - 성공: 도메인 언어 및 애그리게이트 테스트")
    void updateJobBoard_domainLanguageAndAggregate() {
        LocalDateTime now = LocalDateTime.now();
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

        jobBoardUpdateService.updateJobBoard(1L, 1L, command);

        ArgumentCaptor<JobBoard> jobBoardCaptor = ArgumentCaptor.forClass(JobBoard.class);
        verify(jobBoardCommandService, times(1)).save(jobBoardCaptor.capture());
        JobBoard updatedJobBoard = jobBoardCaptor.getValue();
        assertThat(updatedJobBoard.getPostInfo().getTitle()).isEqualTo("New Title");
        assertThat(updatedJobBoard.getPostInfo().getContent()).isEqualTo("New Content");
        assertThat(updatedJobBoard.getCompany().getExperience()).isEqualTo(Experience.JUNIOR);
        assertThat(updatedJobBoard.getCompany().getSalary()).isEqualTo("1000");
    }

    @Test
    @DisplayName("구인구직 게시글 삭제 - 실패: 매니저 아닌 경우")
    void deleteJobBoard_throwsExceptionIfUserIsNotOwner() {
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

        assertThatThrownBy(() -> jobBoardUpdateService.deactivateJobBoard(1L, 1L))
                .isInstanceOf(PostException.class)
                .hasMessage(NO_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("구인구직 업데이트 - 성공: 새로운 테스트 추가")
    void updateJobBoard_newTest() {
        LocalDateTime now = LocalDateTime.now();
        JobBoardUpdateCommand command = new JobBoardUpdateCommand(
                "Updated Title",
                "Updated Content",
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3"),
                now,
                Experience.SENIOR,
                "2000"
        );
        JobBoardResult jobBoard = new JobBoardResult(
                1L,
                "Old Title",
                "Old Content",
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

        jobBoardUpdateService.updateJobBoard(1L, 1L, command);

        ArgumentCaptor<JobBoard> jobBoardCaptor = ArgumentCaptor.forClass(JobBoard.class);
        verify(jobBoardCommandService, times(1)).save(jobBoardCaptor.capture());
        JobBoard updatedJobBoard = jobBoardCaptor.getValue();
        assertThat(updatedJobBoard.getPostInfo().getTitle()).isEqualTo("Updated Title");
        assertThat(updatedJobBoard.getPostInfo().getContent()).isEqualTo("Updated Content");
        assertThat(updatedJobBoard.getCompany().getExperience()).isEqualTo(Experience.SENIOR);
        assertThat(updatedJobBoard.getCompany().getSalary()).isEqualTo("2000");
    }

    @Test
    @DisplayName("구인구직 업데이트 - 성공: 새로운 테스트 추가")
    void updateJobBoard_newTest() {
        LocalDateTime now = LocalDateTime.now();
        JobBoardUpdateCommand command = new JobBoardUpdateCommand(
                "Updated Title",
                "Updated Content",
                List.of("tag1", "tag2"),
                List.of("att1", "att2"),
                List.of("tag3"),
                List.of("att3"),
                now,
                Experience.SENIOR,
                "2000"
        );
        JobBoardResult jobBoard = new JobBoardResult(
                1L,
                "Old Title",
                "Old Content",
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

        jobBoardUpdateService.updateJobBoard(1L, 1L, command);

        ArgumentCaptor<JobBoard> jobBoardCaptor = ArgumentCaptor.forClass(JobBoard.class);
        verify(jobBoardCommandService, times(1)).save(jobBoardCaptor.capture());
        JobBoard updatedJobBoard = jobBoardCaptor.getValue();
        assertThat(updatedJobBoard.getPostInfo().getTitle()).isEqualTo("Updated Title");
        assertThat(updatedJobBoard.getPostInfo().getContent()).isEqualTo("Updated Content");
        assertThat(updatedJobBoard.getCompany().getExperience()).isEqualTo(Experience.SENIOR);
        assertThat(updatedJobBoard.getCompany().getSalary()).isEqualTo("2000");
    }
}
