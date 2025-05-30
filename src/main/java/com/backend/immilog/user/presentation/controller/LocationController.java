package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.usecase.impl.LocationFetchingService;
import com.backend.immilog.user.presentation.payload.UserLoacationPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Location API", description = "위치 관련 API")
@RequestMapping("/api/v1/locations")
@RestController
public class LocationController {
    private final LocationFetchingService locationFetchingService;

    public LocationController(LocationFetchingService locationFetchingService) {
        this.locationFetchingService = locationFetchingService;
    }

    @GetMapping
    @Operation(summary = "위치 정보", description = "위치 정보를 가져옵니다.")
    public ResponseEntity<UserLoacationPayload.UserLocationResponse> getLocation(
            @Parameter(description = "위도") @RequestParam("latitude") Double latitude,
            @Parameter(description = "경도") @RequestParam("longitude") Double longitude
    ) {
        var locationResult = locationFetchingService.getCountry(latitude, longitude).join();
        return ResponseEntity.status(OK).body(
                new UserLoacationPayload.UserLocationResponse(
                        Country.getCountryByKoreanName(locationResult.country()).name(),
                        locationResult.city()
                )
        );
    }
}