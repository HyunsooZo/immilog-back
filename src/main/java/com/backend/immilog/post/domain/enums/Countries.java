package com.backend.immilog.post.domain.enums;

import java.util.Arrays;

public enum Countries {
    ALL("ALL", "ALL", "전체"),
    MALAYSIA("MALAYSIA", "MY", "말레이시아"),
    SINGAPORE("SINGAPORE", "SG", "싱가포르"),
    INDONESIA("INDONESIA", "ID", "인도네시아"),
    VIETNAM("VIETNAM", "VN", "베트남"),
    PHILIPPINES("PHILIPPINES", "PH", "필리핀"),
    THAILAND("THAILAND", "TH", "태국"),
    MYANMAR("MYANMAR", "MM", "미얀마"),
    CAMBODIA("CAMBODIA", "KH", "캄보디아"),
    LAOS("LAOS", "LA", "라오스"),
    BRUNEI("BRUNEI", "BN", "브루나이"),
    EAST_TIMOR("EAST_TIMOR", "TL", "동티모르"),
    CHINA("CHINA", "CN", "중국"),
    JAPAN("JAPAN", "JP", "일본"),
    SOUTH_KOREA("SOUTH_KOREA", "KR", "대한민국"),
    AUSTRALIA("AUSTRALIA", "AU", "오스트레일리아"),
    NEW_ZEALAND("NEW_ZEALAND", "NZ", "뉴질랜드"),
    GUAM("GUAM", "GU", "괌"),
    SAI_PAN("SAI_PAN", "MP", "사이판"),
    ECT("ECT", "ETC", "기타");

    private final String countryName;
    private final String countryCode;
    private final String countryKoreanName;

    Countries(
            String countryName,
            String countryCode,
            String countryKoreanName
    ) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryKoreanName = countryKoreanName;
    }

    public static Countries getCountry(
            String countryName
    ) {
        return Arrays.stream(Countries.values())
                .filter(country -> country.isCountryNameMatch(countryName))
                .findFirst()
                .orElse(null);
    }

    public static Countries getCountryByKoreanName(String countryKoreanName) {
        return Arrays.stream(Countries.values())
                .filter(country -> country.isKoreanNameMatch(countryKoreanName))
                .findFirst()
                .orElse(null);
    }

    public boolean isCountryNameMatch(String countryName) {
        return this.countryName.equals(countryName);
    }

    public boolean isKoreanNameMatch(String koreanName) {
        return this.countryKoreanName.equals(koreanName);
    }
}
