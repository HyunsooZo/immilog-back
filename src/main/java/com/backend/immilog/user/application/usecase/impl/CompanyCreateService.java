package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.application.services.CompanyCommandService;
import com.backend.immilog.user.application.services.CompanyQueryService;
import com.backend.immilog.user.application.usecase.CompanyRegisterUseCase;
import com.backend.immilog.user.domain.model.company.Company;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CompanyCreateService implements CompanyRegisterUseCase {
    private final CompanyCommandService companyCommandService;
    private final CompanyQueryService companyQueryService;
    private final CompanyMapper companyMapper;

    public CompanyCreateService(
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
        company = Objects.isNull(company.seq()) ?
                companyMapper.updateCompany(company, command) :
                companyMapper.toNewCompany(userSeq, command);
        companyCommandService.save(company);
    }

    @Component
    public static class CompanyMapper {
        public Company toNewCompany(
                Long userSeq,
                CompanyRegisterCommand cmd
        ) {
            return Company.builder()
                    .manager(cmd.country(), cmd.region(), userSeq)
                    .companyData(
                            cmd.industry(),
                            cmd.name(),
                            cmd.email(),
                            cmd.phone(),
                            cmd.address(),
                            cmd.homepage(),
                            cmd.logo()
                    );
        }

        public Company updateCompany(
                Company existing,
                CompanyRegisterCommand cmd
        ) {
            return existing.updateAddress(cmd.address())
                    .updateCountry(cmd.country())
                    .updateEmail(cmd.email())
                    .updateHomepage(cmd.homepage())
                    .updateLogo(cmd.logo())
                    .updatePhone(cmd.phone())
                    .updateName(cmd.name())
                    .updateRegion(cmd.region())
                    .updateIndustry(cmd.industry());
        }
    }

}