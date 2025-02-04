package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JobBoardQueryService 테스트")
class JobBoardQueryServiceTest {

    private final JobBoardRepository jobBoardRepository = mock(JobBoardRepository.class);
    private final InteractionUserQueryService interactionUserQueryService = mock(InteractionUserQueryService.class);
    private final PostResourceQueryService postResourceQueryService = mock(PostResourceQueryService.class);
    private final JobBoardQueryService jobBoardQueryService = new JobBoardQueryService(
            jobBoardRepository,
            interactionUserQueryService,
            postResourceQueryService
    );

    @Test
    @DisplayName("getJobBoards 메서드가 JobBoardResult 페이지를 성공적으로 반환")
    void getJobBoardsReturnsJobBoardResultsSuccessfully() {
        Country countryEnum = Country.SOUTH_KOREA;
        String sortingMethod = "date";
        Industry industryEnum = Industry.IT;
        Experience experienceEnum = Experience.SENIOR;
        Pageable pageable = mock(Pageable.class);
        Page<JobBoardResult> expectedPage = new PageImpl<>(List.of(new JobBoardResult(
                1L,
                "title",
                "content",
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                Country.SOUTH_KOREA,
                "region",
                Industry.IT,
                null,
                Experience.SENIOR,
                "salary",
                1L,
                "name",
                "email",
                "phone",
                "address",
                "homepage",
                "logo",
                1L,
                null,
                null
        )));
        when(jobBoardRepository.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable)).thenReturn(expectedPage.map(JobBoard::from));

        Page<JobBoardResult> actualPage = jobBoardQueryService.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable);

        assertThat(actualPage.get().findFirst().get().getSeq()).isEqualTo(expectedPage.get().findFirst().get().getSeq());
    }

    @Test
    @DisplayName("getJobBoards 메서드가 빈 JobBoardResult 페이지를 반환")
    void getJobBoardsReturnsEmptyPage() {
        Country countryEnum = Country.SOUTH_KOREA;
        String sortingMethod = "date";
        Industry industryEnum = Industry.IT;
        Experience experienceEnum = Experience.SENIOR;
        Pageable pageable = mock(Pageable.class);
        Page<JobBoardResult> expectedPage = new PageImpl<>(List.of());
        when(jobBoardRepository.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable)).thenReturn(expectedPage.map(JobBoard::from));

        Page<JobBoardResult> actualPage = jobBoardQueryService.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    @DisplayName("getJobBoardBySeq 메서드가 JobBoardResult를 성공적으로 반환")
    void getJobBoardBySeqReturnsJobBoardResultSuccessfully() {
        Long jobBoardSeq = 1L;
        JobBoardResult expectedJobBoardResult = new JobBoardResult(
                1L,
                "title",
                "content",
                0L,
                0L,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                Country.SOUTH_KOREA,
                "region",
                Industry.IT,
                null,
                Experience.SENIOR,
                "salary",
                1L,
                "name",
                "email",
                "phone",
                "address",
                "homepage",
                "logo",
                1L,
                null,
                null
        );
        when(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)).thenReturn(JobBoard.from(expectedJobBoardResult));

        JobBoardResult actualJobBoardResult = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);

        assertThat(actualJobBoardResult.getSeq()).isEqualTo(expectedJobBoardResult.getSeq());
    }

    @Test
    @DisplayName("getJobBoardBySeq 메서드 예외던짐")
    void getJobBoardBySeqReturnsEmptyOptional() {
        Long jobBoardSeq = 1L;
        when(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)).thenThrow(new PostException(PostErrorCode.JOB_BOARD_NOT_FOUND));
        Assertions.assertThatThrownBy(() -> jobBoardQueryService.getJobBoardBySeq(jobBoardSeq))
                .isInstanceOf(PostException.class)
                .hasMessage(PostErrorCode.JOB_BOARD_NOT_FOUND.getMessage());
    }
}