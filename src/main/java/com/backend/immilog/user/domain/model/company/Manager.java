package com.backend.immilog.user.domain.model.company;

import com.backend.immilog.user.domain.enums.UserCountry;

public record Manager(
        UserCountry country,
        String region,
        Long UserSeq
) {
    public static Manager of(
            UserCountry country,
            String region,
            Long userSeq
    ) {
        return new Manager(country, region, userSeq);
    }
}
