package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class Location {
    @Enumerated(EnumType.STRING)
    private UserCountry country;
    private String region;

    protected Location() {}

    @Builder
    Location(
            UserCountry country,
            String region
    ) {
        this.country = country;
        this.region = region;
    }

    public static Location of(
            UserCountry country,
            String region
    ) {
        return Location.builder()
                .country(country)
                .region(region)
                .build();
    }
}
