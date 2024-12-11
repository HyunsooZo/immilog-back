package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location {
    @Enumerated(EnumType.STRING)
    private UserCountry country;
    private String region;

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
