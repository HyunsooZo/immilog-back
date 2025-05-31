package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.CompanyQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface CompanyFetchUseCase {
    CompanyResult getCompany(Long userSeq);

    @Service
    class CompanyFetcher implements CompanyFetchUseCase {
        private final CompanyQueryService companyQueryService;

        public CompanyFetcher(CompanyQueryService companyQueryService) {
            this.companyQueryService = companyQueryService;
        }

        @Override
        public CompanyResult getCompany(Long userSeq) {
            final var company = companyQueryService.getByCompanyManagerUserSeq(userSeq);
            return Optional.of(CompanyResult.from(company)).orElse(CompanyResult.createEmpty());
        }
    }
}
