package com.backend.immilog.user.application.services.query;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CompanyQueryService {
    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Company> getByCompanyManagerUserSeq(Long userSeq) {
        return companyRepository.getByCompanyManagerUserSeq(userSeq);
    }


}
