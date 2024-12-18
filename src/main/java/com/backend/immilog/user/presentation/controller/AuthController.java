package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.LocationService;
import com.backend.immilog.user.application.services.UserSignInService;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Auth API", description = "인증 관련 API")
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    private final LocationService locationService;
    private final UserSignInService userSignInService;

    public AuthController(
            LocationService locationService,
            UserSignInService userSignInService
    ) {
        this.locationService = locationService;
        this.userSignInService = userSignInService;
    }

    @GetMapping("/user/{userSeq}")
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    public ResponseEntity<UserApiResponse> getUser(
            @PathVariable("userSeq") Long userSeq,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        CompletableFuture<Pair<String, String>> countryFuture = locationService.getCountry(latitude, longitude);
        Pair<String, String> country = locationService.joinCompletableFutureLocation(countryFuture);
        UserSignInResult userSignInResult = userSignInService.getUserSignInDTO(userSeq, country);
        return ResponseEntity.status(OK).body(UserApiResponse.of(userSignInResult));
    }

}

