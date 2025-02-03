package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.result.CompanyResult;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Company;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyInquiryService {
    private final CompanyQueryService companyQueryService;

    public CompanyInquiryService(CompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    public CompanyResult getCompany(Long userSeq) {
        Company company = companyQueryService.getByCompanyManagerUserSeq(userSeq);
        return Optional.of(CompanyResult.from(company)).orElse(CompanyResult.empty());
    }
}
