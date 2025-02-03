package com.backend.immilog.user.domain.model.company;

import com.backend.immilog.global.enums.Country;

public record Manager(
        Country country,
        String region,
        Long userSeq
) {
    public static Manager of(
            Country country,
            String region,
            Long userSeq
    ) {
        return new Manager(country, region, userSeq);
    }

    public static Manager empty() {
        return new Manager(null, null, null);
    }
}
