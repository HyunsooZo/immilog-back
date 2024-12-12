package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.JobBoardUploadCommand;
import com.backend.immilog.post.application.services.command.JobBoardCommandService;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.CompanyInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobBoardUploadService {
    private final JobBoardCommandService jobBoardCommandService;
    private final CompanyInquiryService companyInquiryService;

    public void uploadJobBoard(
            Long userSeq,
            JobBoardUploadCommand command
    ) {
        CompanyResult company = companyInquiryService.getCompany(userSeq);
        JobBoard jobBoard = JobBoard.of(userSeq, company.toDomain(), command);
        jobBoardCommandService.save(jobBoard);
    }
}
