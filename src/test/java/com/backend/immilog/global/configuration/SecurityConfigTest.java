package com.backend.immilog.global.configuration;

import com.backend.immilog.global.filters.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("SecurityConfig 클래스 테스트")
class SecurityConfigTest {
    private final JwtFilter jwtFilter = mock(JwtFilter.class);
    private final HttpSecurity httpSecurity = mock(HttpSecurity.class);
    private final SecurityConfig securityConfig = new SecurityConfig(jwtFilter);

    @BeforeEach
    void setUp() throws Exception {
        mockHttpSecurityChain(httpSecurity);
    }

    private void mockHttpSecurityChain(HttpSecurity http) throws Exception {
        doReturn(http).when(http).csrf(any());
        doReturn(http).when(http).cors(any());
        doReturn(http).when(http).headers(any());
        doReturn(http).when(http).addFilterBefore(any(JwtFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
        doReturn(http).when(http).authorizeHttpRequests(any());
        doReturn(mock(SecurityFilterChain.class)).when(http).build();
    }

    @Test
    @DisplayName("SecurityFilterChain 객체가 정상적으로 생성되는지 테스트")
    void filterChainIsNotNull() throws Exception {
        SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurity);
        assertNotNull(filterChain);
    }

    @Test
    @DisplayName("JwtFilter 객체가 정상적으로 추가되는지 테스트")
    void jwtFilterIsAdded() throws Exception {
        securityConfig.filterChain(httpSecurity);
        verify(httpSecurity).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    @DisplayName("HttpSecurity 객체의 headers 메서드가 정상적으로 호출되는지 테스트")
    void headersConfigurationIsApplied() throws Exception {
        securityConfig.filterChain(httpSecurity);
        verify(httpSecurity).headers(any());
    }

    @Test
    @DisplayName("BCryptPasswordEncoder 객체가 정상적으로 생성되는지 테스트")
    void passwordEncoderIsNotNull() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }
}
