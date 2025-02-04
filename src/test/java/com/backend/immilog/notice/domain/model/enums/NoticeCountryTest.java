package com.backend.immilog.notice.domain.model.enums;

import com.backend.immilog.global.enums.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Enum Country 테스트")
class CountryTest {

    @Test
    @DisplayName("Enum Country의 getCountryName() 메서드 테스트")
    void getCountryName() {
        // given
        Country southKorea = Country.SOUTH_KOREA;

        // when
        String countryName = southKorea.koreanName();
        String countryCode = southKorea.countryCode();

        // then
        assertThat(countryName).isEqualTo("대한민국");
        assertThat(countryCode).isEqualTo("KR");
    }
}