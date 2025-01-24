package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Location {
    private UserCountry country;
    private String region;

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
        return new Location(country, region);
    }

    protected void updateLocation(
            UserCountry value,
            String region
    ) {
        this.country = value;
        this.region = region;
    }

}
