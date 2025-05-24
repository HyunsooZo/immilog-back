package com.backend.immilog.user.application.services;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyCommandService {
    private final CompanyRepository companyRepository;

    public CompanyCommandService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public void save(Company company) {
        companyRepository.save(company);
    }
}
