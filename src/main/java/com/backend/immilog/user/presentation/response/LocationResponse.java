package com.backend.immilog.user.presentation.response;

import com.backend.immilog.global.enums.Country;
import lombok.Builder;

@Builder
public record LocationResponse(
        String country,
        String region
) {
    public static LocationResponse from(
            Country country,
            String region
    ) {
        return LocationResponse.builder()
                .country(country.name())
                .region(region)
                .build();
    }
}
