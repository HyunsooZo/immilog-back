package com.backend.immilog.global.security;

import com.backend.immilog.global.aop.UserIdExtractionAspect;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserIdExtractionAspect 클래스 테스트")
class UserIdExtractionAspectTest {

    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final UserIdExtractionAspect userIdExtractionAspect = new UserIdExtractionAspect(
            tokenProvider,
            request
    );

    @Test
    @DisplayName("Authorization 헤더가 없는 경우 extractUserId가 예외를 던지지 않는지 테스트")
    void extractUserIdDoesNotThrowExceptionWhenAuthorizationHeaderIsMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);
        userIdExtractionAspect.extractUserDetails();
        assertNull(request.getAttribute("userSeq"));
    }

    @Test
    @DisplayName("Authorization 헤더가 없는 경우 extractUserRole이 예외를 던지지 않는지 테스트")
    void extractUserRoleDoesNotThrowExceptionWhenAuthorizationHeaderIsMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);
        userIdExtractionAspect.extractUserDetails();
        assertNull(request.getAttribute("userRole"));
    }
}