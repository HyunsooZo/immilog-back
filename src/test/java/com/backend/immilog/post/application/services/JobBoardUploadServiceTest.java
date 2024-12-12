package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.application.services.command.JobBoardCommandService;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.CompanyInquiryService;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("JobBoardUploadService 테스트")
class JobBoardUploadServiceTest {
    private final JobBoardCommandService jobBoardCommandService = mock(JobBoardCommandService.class);
    private final CompanyInquiryService companyInquiryService = mock(CompanyInquiryService.class);
    private final JobBoardUploadService jobBoardUploadService = new JobBoardUploadService(jobBoardCommandService, companyInquiryService);

    @Test
    @DisplayName("구인구직 업로드 : 성공")
    void uploadJobBoard() {
        // given
        Long userSeq = 1L;
        JobBoardUploadCommand command = JobBoardUploadCommand.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .viewCount(0L)
                .likeCount(0L)
                .tags(List.of("tag1", "tag2"))
                .attachments(List.of("attachment1", "attachment2"))
                .deadline(LocalDateTime.now())
                .experience(Experience.JUNIOR)
                .salary("salary")
                .companySeq(1L)
                .status(PostStatus.NORMAL)
                .build();
        User user = User.builder()
                .seq(1L)
                .email("user@email.com")
                .nickName("user")
                .imageUrl("image")
                .build();
        CompanyResult company = CompanyResult.builder()
                .seq(1L)
                .companyAddress("address")
                .companyName("company")
                .companyCountry(UserCountry.SOUTH_KOREA)
                .companyRegion("region")
                .build();

        when(companyInquiryService.getCompany(userSeq)).thenReturn(company);

        // when
        jobBoardUploadService.uploadJobBoard(userSeq, command);

        // then
        verify(jobBoardCommandService).save(any());
    }
}