package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.services.LocationService;
import com.backend.immilog.user.presentation.response.UserGeneralResponse;
import com.backend.immilog.user.presentation.response.UserLocationResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.global.enums.Country.SOUTH_KOREA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@Disabled // 현재 구글 페이 관련 문제있어 임시 보류
@DisplayName("LocationController 테스트")
class LocationControllerTest {
    private final LocationService locationService = mock(LocationService.class);
    private final LocationController locationController = new LocationController(locationService);

    @Test
    @DisplayName("위치 정보 가져오기")
    void getLocation() {
        // given
        Double latitude = 37.5665;
        Double longitude = 126.9780;
        String country = "대한민국";
        String countryCode = "KR";

        Pair<String, String> countryPair = Pair.of(country, countryCode);

        when(locationService.getCountry(latitude, longitude)).thenReturn(CompletableFuture.completedFuture(countryPair));

        // when
        ResponseEntity<UserLocationResponse> response = locationController.getLocation(latitude, longitude);

        // then
        assertThat(response).isNotNull();
        assertThat(OK).isEqualTo(response.getStatusCode());
        UserLocationResponse.LocationResponse locationResponse = Objects.requireNonNull(response.getBody()).data();
        assertThat(locationResponse).isNotNull();
        System.out.println(locationResponse.country());
        assertThat(Country.getCountryByKoreanName(country).name()).isEqualTo(locationResponse.country());
        assertThat(SOUTH_KOREA.toString()).isEqualTo(locationResponse.country());
    }
}
