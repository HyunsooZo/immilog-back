package com.backend.immilog.user.application.services.query;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyQueryService {
    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Company getByCompanyManagerUserSeq(Long userSeq) {
        return companyRepository
                .getByCompanyManagerUserSeq(userSeq)
                .orElse(Company.empty());
    }


}
