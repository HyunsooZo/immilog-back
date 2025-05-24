package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.user.application.usecase.LocationFetchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LocationFetchingService implements LocationFetchUseCase {
    private final RestTemplate restTemplate;
    @Value("${geocode.url}")
    private String geocoderUrl;
    @Value("${geocode.key}")
    private String geocoderKey;

    public LocationFetchingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @Override
    public CompletableFuture<Pair<String, String>> getCountry(
            Double latitude,
            Double longitude
    ) {
        if (latitude <= 0.0 || longitude <= 0.0) {
            return CompletableFuture.completedFuture(null);
        }
        try {
            var response = this.generateRestTemplate(latitude, longitude);
            log.info(String.format(geocoderUrl, latitude, longitude, geocoderKey));
            var compoundCode = extractCompoundCode(response.join());
            var parts = Objects.requireNonNull(compoundCode).split(" ");
            if (parts.length >= 3) {
                var country = parts[1];
                var city = parts[2];
                return CompletableFuture.completedFuture(Pair.of(country, city));
            }
        } catch (Exception e) {
            log.error("Geocoder API 호출 중 예외 발생", e);
        }
        return CompletableFuture.completedFuture(Pair.of("기타 국가", "기타 지역"));
    }

    private CompletableFuture<String> generateRestTemplate(
            Double latitude,
            Double longitude
    ) {
        return CompletableFuture.supplyAsync(() -> {
            var url = String.format(geocoderUrl, latitude, longitude, geocoderKey);
            return restTemplate.getForObject(url, String.class);
        });
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

    @Override
    public Pair<String, String> joinCompletableFutureLocation(CompletableFuture<Pair<String, String>> countryFuture) {
        return countryFuture
                .orTimeout(5, TimeUnit.SECONDS) // 5초 이내에 완료되지 않으면 타임아웃
                .exceptionally(throwable -> Pair.of("Error", "Timeout"))
                .join();
    }
}
