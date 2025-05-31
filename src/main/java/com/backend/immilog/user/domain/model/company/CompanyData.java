package com.backend.immilog.user.domain.model.company;

public record CompanyData(
        Industry industry,
        String name,
        String email,
        String phone,
        String address,
        String homepage,
        String logo
) {
    public static CompanyData of(
            Industry industry,
            String name,
            String email,
            String phone,
            String address,
            String homepage,
            String logo
    ) {
        return new CompanyData(industry, name, email, phone, address, homepage, logo);
    }

    public static CompanyData createEmpty() {
        return new CompanyData(null, null, null, null, null, null, null);
    }
}
