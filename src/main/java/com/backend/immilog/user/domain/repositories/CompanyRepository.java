package com.backend.immilog.user.domain.repositories;

import com.backend.immilog.user.domain.model.company.Company;

import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> getByCompanyManagerUserSeq(Long userSeq);

    void saveEntity(Company of);
}
