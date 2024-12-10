package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInquiryService {
    private final CompanyQueryService companyQueryService;

    public CompanyResult getCompany(
            Long userSeq
    ) {
        return companyQueryService.getByCompanyManagerUserSeq(userSeq)
                .map(CompanyResult::from)
                .orElse(CompanyResult.builder().build());
    }
}
