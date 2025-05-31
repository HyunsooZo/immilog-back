package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.CompanyCommandService;
import com.backend.immilog.user.application.services.CompanyMapper;
import com.backend.immilog.user.application.services.CompanyQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public interface CompanyCreateUseCase {
    void registerOrEditCompany(
            Long userSeq,
            CompanyRegisterCommand command
    );

    @Service
    class CompanyCreator implements CompanyCreateUseCase {
        private final CompanyCommandService companyCommandService;
        private final CompanyQueryService companyQueryService;
        private final CompanyMapper companyMapper;

        public CompanyCreator(
                CompanyCommandService companyCommandService,
                CompanyQueryService companyQueryService,
                CompanyMapper companyMapper
        ) {
            this.companyCommandService = companyCommandService;
            this.companyQueryService = companyQueryService;
            this.companyMapper = companyMapper;
        }

        @Transactional
        @Override
        public void registerOrEditCompany(
                Long userSeq,
                CompanyRegisterCommand command
        ) {
            var company = companyQueryService.getByCompanyManagerUserSeq(userSeq);
            companyCommandService.save(
                    Objects.isNull(company.seq()) ?
                            companyMapper.toNewCompany(userSeq, command) :
                            companyMapper.updateCompany(company, command)
            );
        }
    }
}
