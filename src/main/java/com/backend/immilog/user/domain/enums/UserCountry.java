package com.backend.immilog.user.domain.enums;

import com.backend.immilog.global.enums.GlobalCountry;

public enum UserCountry {
    ALL("전체"),
    MALAYSIA("말레이시아"),
    SINGAPORE("싱가포르"),
    INDONESIA("인도네시아"),
    VIETNAM("베트남"),
    PHILIPPINES("필리핀"),
    THAILAND("태국"),
    MYANMAR("미얀마"),
    CAMBODIA("캄보디아"),
    LAOS("라오스"),
    BRUNEI("브루나이"),
    EAST_TIMOR("동티모르"),
    CHINA("중국"),
    JAPAN("일본"),
    SOUTH_KOREA("대한민국"),
    AUSTRALIA("오스트레일리아"),
    NEW_ZEALAND("뉴질랜드"),
    GUAM("괌"),
    SAI_PAN("사이판"),
    ECT("기타");

    private final String countryKoreanName;

    UserCountry(String countryKoreanName) {
        this.countryKoreanName = countryKoreanName;
    }

    public GlobalCountry toGlobalCountries() {
        return GlobalCountry.valueOf(this.name());
    }

    public String koreanName() { return this.countryKoreanName; }
}
