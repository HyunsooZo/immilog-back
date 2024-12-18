package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import org.springframework.stereotype.Service;

@Service
public class CompanyInquiryService {
    private final CompanyQueryService companyQueryService;

    public CompanyInquiryService(CompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    public CompanyResult getCompany(Long userSeq) {
        return companyQueryService.getByCompanyManagerUserSeq(userSeq)
                .map(CompanyResult::from)
                .orElse(CompanyResult.builder().build());
    }
}
