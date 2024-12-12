package com.backend.immilog.global.filters;

import com.backend.immilog.global.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("JwtFilter 클래스")
class JwtFilterTest {
    private final JwtProvider jwtProvider = mock(JwtProvider.class);
    private final JwtFilter jwtFilter = new JwtFilter(jwtProvider);

    @Test
    @DisplayName("화이트리스트에 있는 URI인 경우 필터링을 건너뛴다")
    void shouldNotFilterIfRequestIsInWhiteList() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/users/login");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtProvider, never()).getAuthentication(anyString());
    }

    @Test
    @DisplayName("토큰이 유효한 경우 인증을 진행한다")
    void shouldAuthenticateIfTokenIsValid() throws ServletException, IOException {
        // given
        String token = "validToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/protected"); // WhiteList에 없는 URI
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        Authentication authentication = mock(Authentication.class);
        when(jwtProvider.getAuthentication(token)).thenReturn(authentication);

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(jwtProvider, times(1)).getAuthentication(token);
        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
    }

    @Test
    @DisplayName("토큰이 유효하지 않은 경우 401 에러를 반환한다")
    void shouldReturnUnauthorizedIfTokenIsInvalid() throws ServletException, IOException {
        // given
        String token = "invalidToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/protected"); // WhiteList에 없는 URI
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtProvider.getAuthentication(token)).thenThrow(new RuntimeException("Invalid token"));

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(jwtProvider, times(1)).getAuthentication(token);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }
}
