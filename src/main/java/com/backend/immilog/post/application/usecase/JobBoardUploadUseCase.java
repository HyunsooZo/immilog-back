package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.application.services.JobBoardCommandService;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.user.application.usecase.CompanyFetchUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface JobBoardUploadUseCase {
    void uploadJobBoard(
            Long userSeq,
            JobBoardUploadCommand command
    );

    @Slf4j
    @Service
    class JobBoardUploader implements JobBoardUploadUseCase {
        private final JobBoardCommandService jobBoardCommandService;
        private final CompanyFetchUseCase.CompanyFetcher companyFetcher;

        public JobBoardUploader(
                JobBoardCommandService jobBoardCommandService,
                CompanyFetchUseCase.CompanyFetcher companyFetcher
        ) {
            this.jobBoardCommandService = jobBoardCommandService;
            this.companyFetcher = companyFetcher;
        }

        public void uploadJobBoard(
                Long userSeq,
                JobBoardUploadCommand command
        ) {
            var company = companyFetcher.getCompany(userSeq);
            jobBoardCommandService.save(JobBoard.of(
                    userSeq,
                    company.toDomain(),
                    command.title(),
                    command.content(),
                    command.experience(),
                    command.deadline(),
                    command.salary()
            ));
        }
    }
}
