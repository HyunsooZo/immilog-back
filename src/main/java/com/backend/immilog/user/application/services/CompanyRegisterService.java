package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.command.CompanyCommandService;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Service
public class CompanyRegisterService {
    private final CompanyQueryService companyQueryService;
    private final CompanyCommandService companyCommandService;

    @Transactional
    public void registerOrEditCompany(
            Long userSeq,
            CompanyRegisterCommand command
    ) {
        AtomicBoolean isExistingCompany = new AtomicBoolean(false);
        checkIfExistingAndProceedWithUpdate(userSeq, command, isExistingCompany);
        if (!isExistingCompany.get()) {
            companyCommandService.save(Company.of(userSeq, command));
        }
    }

    private void checkIfExistingAndProceedWithUpdate(
            Long userSeq,
            CompanyRegisterCommand request,
            AtomicBoolean isExistingCompany
    ) {
        companyQueryService.getByCompanyManagerUserSeq(userSeq)
                .ifPresent(company -> {
                    updateCompany(company, request);
                    isExistingCompany.set(true);
                });
    }

    private void updateCompany(
            Company company,
            CompanyRegisterCommand request
    ) {
        company.updateCompanyAddress(request.companyAddress());
        company.updateCompanyCountry(request.companyCountry());
        company.updateCompanyEmail(request.companyEmail());
        company.updateCompanyHomepage(request.companyHomepage());
        company.updateCompanyLogo(request.companyLogo());
        company.updateCompanyPhone(request.companyPhone());
        company.updateCompanyName(request.companyName());
        company.updateCompanyRegion(request.companyRegion());
        company.updateCompanyIndustry(request.industry());
        companyCommandService.save(company);
    }
}