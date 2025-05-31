package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.usecase.LocationFetchUseCase;
import com.backend.immilog.user.application.usecase.UserSignInUseCase;
import com.backend.immilog.user.presentation.payload.UserSignInPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("인증 컨트롤러 테스트")
class AuthControllerTest {
    private final LocationFetchUseCase.LocationFetcher locationFetcher = mock(LocationFetchUseCase.LocationFetcher.class);
    private final UserSignInUseCase.UserLoginProcessor userLoginProcessor = mock(UserSignInUseCase.UserLoginProcessor.class);
    private final AuthController authController = new AuthController(
            locationFetcher,
            userLoginProcessor
    );

    @Test
    @DisplayName("사용자 정보 조회")
    void getUser() {
        // given
        Long userSeq = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        Double latitude = 37.123456;
        Double longitude = 126.123456;
        String mail = "test@email.com";
        UserSignInResult userSignInResult = new UserSignInResult(
                userSeq,
                mail,
                "test",
                "accessToken",
                "refreshToken",
                "South Korea",
                "South Korea",
                "Seoul",
                "image",
                true
        );
        LocationResult location = new LocationResult("KR", "South Korea");
        CompletableFuture<LocationResult> value = CompletableFuture.completedFuture(location);
        when(locationFetcher.getCountry(latitude, longitude)).thenReturn(value);
        when(locationFetcher.joinCompletableFutureLocation(value)).thenReturn(location);
        when(userLoginProcessor.getUserSignInDTO(userSeq, location)).thenReturn(userSignInResult);
        when(request.getAttribute("userSeq")).thenReturn(1L);

        // when
        ResponseEntity<UserSignInPayload.UserSignInResponse> response = authController.getUser(userSeq, latitude, longitude);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        Object data = Objects.requireNonNull(response.getBody()).data();
        assertThat(((UserSignInResult) data).email()).isEqualTo(userSignInResult.email());
    }
}