package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class LocationValue {

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Column(name = "region")
    private String region;

    protected LocationValue() {}

    protected LocationValue(
            Country country,
            String region
    ) {
        this.country = country;
        this.region = region;
    }

    public static LocationValue of(
            Country country,
            String region
    ) {
        return new LocationValue(country, region);
    }

    public Location toDomain() {
        if (this.country == null && this.region == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return Location.of(this.country, this.region);
    }
}
