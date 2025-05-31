package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.infrastructure.gateway.GeocodeGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface LocationFetchUseCase {
    CompletableFuture<LocationResult> getCountry(
            Double latitude,
            Double longitude
    );

    LocationResult joinCompletableFutureLocation(CompletableFuture<LocationResult> countryFuture);

    @Slf4j
    @Service
    class LocationFetcher implements LocationFetchUseCase {
        private final GeocodeGateway geocodeGateway;

        public LocationFetcher(GeocodeGateway geocodeGateway) {
            this.geocodeGateway = geocodeGateway;
        }

        @Async
        @Override
        public CompletableFuture<LocationResult> getCountry(
                Double latitude,
                Double longitude
        ) {
            if (latitude <= 0.0 || longitude <= 0.0) {
                return CompletableFuture.completedFuture(null);
            }
            try {
                var response = CompletableFuture.supplyAsync(() -> geocodeGateway.fetchGeocode(latitude, longitude));
                var compoundCode = extractCompoundCode(response.join());
                var parts = Objects.requireNonNull(compoundCode).split(" ");
                if (parts.length >= 3) {
                    var country = parts[1];
                    var city = parts[2];
                    return CompletableFuture.completedFuture(new LocationResult(country, city));
                }
            } catch (Exception e) {
                log.error("Geocoder API 호출 중 예외 발생", e);
            }
            return CompletableFuture.completedFuture(new LocationResult("기타 국가", "기타 지역"));
        }

        @Override
        public LocationResult joinCompletableFutureLocation(CompletableFuture<LocationResult> countryFuture) {
            return countryFuture
                    .orTimeout(5, TimeUnit.SECONDS)
                    .exceptionally(throwable -> new LocationResult("Error", "Timeout"))
                    .join();
        }

        private String extractCompoundCode(String jsonResponse) {
            var objectMapper = new ObjectMapper();
            try {
                var rootNode = objectMapper.readTree(jsonResponse);
                var plusCodeNode = rootNode.path("plus_code");
                if (!plusCodeNode.isMissingNode()) {
                    return plusCodeNode.path("compound_code").asText();
                }
            } catch (IOException e) {
                log.error("JSON 파싱 중 예외 발생", e);
            }
            return null;
        }

    }
}
