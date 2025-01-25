package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;

public record Location(
        UserCountry country,
        String region
) {
    public static Location of(
            UserCountry country,
            String region
    ) {
        return new Location(country, region);
    }
}
