package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("JobBoardCommandService 테스트")
class JobBoardCommandServiceTest {

    private final JobBoardRepository jobBoardRepository = mock(JobBoardRepository.class);
    private final JobBoardCommandService jobBoardCommandService = new JobBoardCommandService(jobBoardRepository);

    @Test
    @DisplayName("save 메서드가 JobBoard를 성공적으로 저장")
    void saveSavesJobBoardSuccessfully() {
        JobBoard jobBoard = new JobBoard(
                1L,
                1L,
                null,
                null,
                null,
                null
        );

        jobBoardCommandService.save(jobBoard);

        ArgumentCaptor<JobBoard> jobBoardCaptor = ArgumentCaptor.forClass(JobBoard.class);
        verify(jobBoardRepository).save(jobBoardCaptor.capture());

        assertThat(jobBoardCaptor.getValue()).isEqualTo(jobBoard);
    }

    @Test
    @DisplayName("save 메서드가 null JobBoard를 처리")
    void saveHandlesNullJobBoard() {
        JobBoard jobBoard = null;

        jobBoardCommandService.save(jobBoard);

        ArgumentCaptor<JobBoard> jobBoardCaptor = ArgumentCaptor.forClass(JobBoard.class);
        verify(jobBoardRepository).save(jobBoardCaptor.capture());

        assertThat(jobBoardCaptor.getValue()).isNull();
    }
}