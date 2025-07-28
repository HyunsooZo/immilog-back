package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.user.domain.enums.Country;
import com.backend.immilog.user.domain.model.Location;
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
public class LocationJpaValue {

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Column(name = "region")
    private String region;

    protected LocationJpaValue() {}

    protected LocationJpaValue(
            Country country,
            String region
    ) {
        this.country = country;
        this.region = region;
    }

    public static LocationJpaValue of(
            Country country,
            String region
    ) {
        return new LocationJpaValue(country, region);
    }

    public static LocationJpaValue from(Location location) {
        return new LocationJpaValue(
                location.country(),
                location.region()
        );
    }

    public Location toDomain() {
        if (this.country == null && this.region == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return Location.of(this.country, this.region);
    }
}
