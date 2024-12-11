package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.aop.ExtractUserId;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.LocationService;
import com.backend.immilog.user.application.services.UserSignInService;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Auth API", description = "인증 관련 API")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final LocationService locationService;
    private final UserSignInService userSignInService;

    @GetMapping("/user")
    @ExtractUserId
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    public ResponseEntity<UserApiResponse> getUser(
            HttpServletRequest request,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        CompletableFuture<Pair<String, String>> countryFuture =
                locationService.getCountry(latitude, longitude);

        Pair<String, String> country =
                locationService.joinCompletableFutureLocation(countryFuture);

        Long userSeq = (Long) request.getAttribute("userSeq");

        UserSignInResult userSignInResult =
                userSignInService.getUserSignInDTO(userSeq, country);

        return ResponseEntity
                .status(OK)
                .body(UserApiResponse.of(userSignInResult));
    }

}

