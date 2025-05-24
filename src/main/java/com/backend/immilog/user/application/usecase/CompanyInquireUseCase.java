package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.result.CompanyResult;

public interface CompanyInquireUseCase {
    CompanyResult getCompany(Long userSeq);
}
