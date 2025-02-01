package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.Country;

public record Location(
        Country country,
        String region
) {
    public static Location of(
            Country country,
            String region
    ) {
        return new Location(country, region);
    }
}
