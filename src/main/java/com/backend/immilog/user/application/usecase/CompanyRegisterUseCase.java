package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;

public interface CompanyRegisterUseCase {
    void registerOrEditCompany(
            Long userSeq,
            CompanyRegisterCommand command
    );
}
