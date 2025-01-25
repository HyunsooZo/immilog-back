package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JobBoardQueryService 테스트")
class JobBoardQueryServiceTest {

    private final JobBoardRepository jobBoardRepository = mock(JobBoardRepository.class);
    private final JobBoardQueryService jobBoardQueryService = new JobBoardQueryService(jobBoardRepository);

    @Test
    @DisplayName("getJobBoards 메서드가 JobBoardResult 페이지를 성공적으로 반환")
    void getJobBoardsReturnsJobBoardResultsSuccessfully() {
        Countries countryEnum = Countries.SOUTH_KOREA;
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
                Countries.SOUTH_KOREA,
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
        when(jobBoardRepository.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable)).thenReturn(expectedPage);

        Page<JobBoardResult> actualPage = jobBoardQueryService.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    @DisplayName("getJobBoards 메서드가 빈 JobBoardResult 페이지를 반환")
    void getJobBoardsReturnsEmptyPage() {
        Countries countryEnum = Countries.SOUTH_KOREA;
        String sortingMethod = "date";
        Industry industryEnum = Industry.IT;
        Experience experienceEnum = Experience.SENIOR;
        Pageable pageable = mock(Pageable.class);
        Page<JobBoardResult> expectedPage = new PageImpl<>(List.of());
        when(jobBoardRepository.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable)).thenReturn(expectedPage);

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
                Countries.SOUTH_KOREA,
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
        when(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)).thenReturn(Optional.of(expectedJobBoardResult));

        Optional<JobBoardResult> actualJobBoardResult = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);

        assertThat(actualJobBoardResult).isPresent();
        assertThat(actualJobBoardResult.get()).isEqualTo(expectedJobBoardResult);
    }

    @Test
    @DisplayName("getJobBoardBySeq 메서드가 빈 Optional을 반환")
    void getJobBoardBySeqReturnsEmptyOptional() {
        Long jobBoardSeq = 1L;
        when(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)).thenReturn(Optional.empty());

        Optional<JobBoardResult> actualJobBoardResult = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);

        assertThat(actualJobBoardResult).isEmpty();
    }
}