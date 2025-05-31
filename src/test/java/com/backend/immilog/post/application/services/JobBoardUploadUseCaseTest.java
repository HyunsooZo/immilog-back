package com.backend.immilog.post.application.services;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.application.usecase.JobBoardUploadUseCase;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.PostStatus;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.usecase.CompanyFetchUseCase;
import com.backend.immilog.user.domain.model.company.Industry;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("JobBoardUploadService 테스트")
class JobBoardUploadUseCaseTest {
    private final JobBoardCommandService jobBoardCommandService = mock(JobBoardCommandService.class);
    private final CompanyFetchUseCase.CompanyFetcher companyFetcher = mock(CompanyFetchUseCase.CompanyFetcher.class);
    private final JobBoardUploadUseCase jobBoardUploadUseCase = new JobBoardUploadUseCase.JobBoardUploader(jobBoardCommandService, companyFetcher);

    @Test
    @DisplayName("구인구직 업로드 : 성공")
    void uploadJobBoard() {
        // given
        Long userSeq = 1L;
        //public record JobBoardUploadCommand(
        //        Long seq,
        //        String title,
        //        String content,
        //        Long viewCount,
        //        Long likeCount,
        //        List<String> tags,
        //        List<String> attachments,
        //        LocalDateTime deadline,
        //        Experience experience,
        //        String salary,
        //        Long companySeq,
        //        PostStatus status
        //) {
        JobBoardUploadCommand command = new JobBoardUploadCommand(
                1L,
                "title",
                "content",
                0L,
                0L,
                List.of("tag1", "tag2"),
                List.of("attachment1", "attachment2"),
                LocalDateTime.now(),
                Experience.JUNIOR,
                "salary",
                1L,
                PostStatus.NORMAL
        );
        User user = new User(
                1L,
                Auth.of("email@email.com", null),
                UserRole.ROLE_USER,
                new ReportData(0L, null),
                Profile.of("user", "image", null),
                new Location(Country.SOUTH_KOREA, "region"),
                UserStatus.PENDING,
                null
        );
        CompanyResult company = new CompanyResult(
                1L,
                Industry.IT,
                "company",
                "email@email.com",
                "010-1234-5678",
                "address",
                "homepage",
                Country.SOUTH_KOREA,
                "region",
                "logo",
                1L
        );

        when(companyFetcher.getCompany(userSeq)).thenReturn(company);

        // when
        jobBoardUploadUseCase.uploadJobBoard(userSeq, command);

        // then
        verify(jobBoardCommandService).save(any());
    }
}