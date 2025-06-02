package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.usecase.LocationFetchUseCase;
import com.backend.immilog.user.application.usecase.UserSignInUseCase;
import com.backend.immilog.user.presentation.payload.UserSignInPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Auth API", description = "인증 관련 API")
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    private final LocationFetchUseCase locationFetcher;
    private final UserSignInUseCase userLoginProcessor;

    public AuthController(
            LocationFetchUseCase locationFetcher,
            UserSignInUseCase userLoginProcessor
    ) {
        this.locationFetcher = locationFetcher;
        this.userLoginProcessor = userLoginProcessor;
    }

    @GetMapping("/user/{userSeq}")
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    public ResponseEntity<UserSignInPayload.UserSignInResponse> getUser(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "위도") @RequestParam("latitude") Double latitude,
            @Parameter(description = "경도") @RequestParam("longitude") Double longitude
    ) {
        var countryFuture = locationFetcher.getCountry(latitude, longitude);
        var country = locationFetcher.joinCompletableFutureLocation(countryFuture);
        var userSignInResult = userLoginProcessor.getUserSignInDTO(userSeq, country);
        return ResponseEntity.status(OK).body(userSignInResult.toResponse());
    }

}

