package com.backend.immilog.company.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, Long> {
    Optional<CompanyJpaEntity> findByManager_CompanyManagerUserSeq(Long userSeq);

    boolean existsByCompanyMetaData_CompanyName(String name);
}

