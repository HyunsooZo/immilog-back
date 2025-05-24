package com.backend.immilog.user.application.command;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.company.Industry;

public record CompanyRegisterCommand(
        Industry industry,
        String name,
        String email,
        String phone,
        String address,
        String homepage,
        Country country,
        String region,
        String logo
) {
}