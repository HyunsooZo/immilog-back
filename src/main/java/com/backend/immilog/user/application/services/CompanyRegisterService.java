package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.command.CompanyCommandService;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CompanyRegisterService {
    private final CompanyQueryService companyQueryService;
    private final CompanyCommandService companyCommandService;

    public CompanyRegisterService(
            CompanyQueryService companyQueryService,
            CompanyCommandService companyCommandService
    ) {
        this.companyQueryService = companyQueryService;
        this.companyCommandService = companyCommandService;
    }

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
        Company updatedCompany = company.updateAddress(request.companyAddress())
                .updateCountry(request.companyCountry())
                .updateEmail(request.companyEmail())
                .updateHomepage(request.companyHomepage())
                .updateLogo(request.companyLogo())
                .updatePhone(request.companyPhone())
                .updateName(request.companyName())
                .updateRegion(request.companyRegion())
                .updateIndustry(request.industry());

        companyCommandService.save(updatedCompany);
    }
}