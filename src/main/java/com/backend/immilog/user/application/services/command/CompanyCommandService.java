package com.backend.immilog.user.application.services.command;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyCommandService {
    private final CompanyRepository companyRepository;


    @Transactional
    public void save(Company company) {
        companyRepository.saveEntity(company);
    }
}
