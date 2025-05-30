package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.CompanyQueryService;
import com.backend.immilog.user.application.usecase.CompanyInquireUseCase;
import com.backend.immilog.user.domain.model.company.Company;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyFetchService implements CompanyInquireUseCase {
    private final CompanyQueryService companyQueryService;

    public CompanyFetchService(CompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    @Override
    public CompanyResult getCompany(Long userSeq) {
        final var company = companyQueryService.getByCompanyManagerUserSeq(userSeq);
        return Optional.of(CompanyResult.from(company)).orElse(CompanyResult.empty());
    }
}
