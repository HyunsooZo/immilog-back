package com.backend.immilog.user.infrastructure.repositories;

import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.repositories.CompanyRepository;
import com.backend.immilog.user.infrastructure.jpa.entity.CompanyEntity;
import com.backend.immilog.user.infrastructure.jpa.repositories.CompanyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyJpaRepository companyJpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Optional<Company> getByCompanyManagerUserSeq(
            Long userSeq
    ) {
        return companyJpaRepository
                .findByCompanyManagerUserSeq(userSeq)
                .map(CompanyEntity::toDomain);
    }

    @Override
    public void saveEntity(
            Company company
    ) {
        companyJpaRepository.save(CompanyEntity.from(company));
    }
}
