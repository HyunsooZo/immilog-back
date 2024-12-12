package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.enums.GlobalCountry;

import com.backend.immilog.user.application.services.LocationService;
import com.backend.immilog.user.presentation.response.LocationResponse;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.global.enums.GlobalCountry.SOUTH_KOREA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

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

        when(locationService.getCountry(latitude, longitude))
                .thenReturn(CompletableFuture.completedFuture(countryPair));

        // when
        ResponseEntity<?> response = locationController.getLocation(latitude,
                longitude);

        // then
        assertThat(response).isNotNull();
        assertThat(OK).isEqualTo(response.getStatusCode());
        LocationResponse locationResponse =
                (LocationResponse) ((UserApiResponse) Objects.requireNonNull(response.getBody())).data();
        assertThat(locationResponse).isNotNull();
        assertThat(GlobalCountry.getCountryByKoreanName(country).getCountryName())
                .isEqualTo(locationResponse.country());
        assertThat(SOUTH_KOREA.toString()).isEqualTo(locationResponse.country());
    }
}
