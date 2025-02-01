package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.command.CompanyCommandService;
import com.backend.immilog.user.application.services.query.CompanyQueryService;
import com.backend.immilog.user.domain.model.company.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
        validateExistingCompany(userSeq, command, isExistingCompany);
        if (!isExistingCompany.get()) {
            Company newCompany = createCompany(userSeq, command);
            companyCommandService.save(newCompany);
        }
    }

    private static Company createCompany(
            Long userSeq,
            CompanyRegisterCommand command
    ) {
        return Company.withNew()
                .manager(
                        command.country(),
                        command.region(),
                        userSeq
                )
                .companyData(
                        command.industry(),
                        command.name(),
                        command.email(),
                        command.phone(),
                        command.address(),
                        command.homepage(),
                        command.logo()
                );
    }

    private void validateExistingCompany(
            Long userSeq,
            CompanyRegisterCommand command,
            AtomicBoolean isExistingCompany
    ) {
        Optional.ofNullable(companyQueryService.getByCompanyManagerUserSeq(userSeq))
                .ifPresent(company -> {
                    updateCompany(company, command);
                    isExistingCompany.set(true);
                });
    }

    private void updateCompany(
            Company company,
            CompanyRegisterCommand request
    ) {
        Company updatedCompany = company.updateAddress(request.address())
                .updateCountry(request.country())
                .updateEmail(request.email())
                .updateHomepage(request.homepage())
                .updateLogo(request.logo())
                .updatePhone(request.phone())
                .updateName(request.name())
                .updateRegion(request.region())
                .updateIndustry(request.industry());

        companyCommandService.save(updatedCompany);
    }
}