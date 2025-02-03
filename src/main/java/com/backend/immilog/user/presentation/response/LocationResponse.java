package com.backend.immilog.user.presentation.response;

import com.backend.immilog.global.enums.Country;

public record LocationResponse(
        String country,
        String region
) {
    public static LocationResponse from(
            Country country,
            String region
    ) {
        return new LocationResponse(country.name(), region);
    }
}
