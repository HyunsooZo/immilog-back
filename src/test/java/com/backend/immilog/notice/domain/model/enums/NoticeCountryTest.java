package com.backend.immilog.notice.domain.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Enum NoticeCountry 테스트")
class NoticeCountryTest {

    @Test
    @DisplayName("Enum NoticeCountry의 getCountryName() 메서드 테스트")
    void getCountryName() {
        // given
        NoticeCountry southKorea = NoticeCountry.SOUTH_KOREA;

        // when
        String countryName = southKorea.getCountryKoreanName();
        String countryCode = southKorea.getCountryCode();

        // then
        assertThat(countryName).isEqualTo("대한민국");
        assertThat(countryCode).isEqualTo("KR");
    }
}