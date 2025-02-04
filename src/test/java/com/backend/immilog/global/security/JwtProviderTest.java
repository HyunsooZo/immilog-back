package com.backend.immilog.global.security;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("JwtProvider 클래스")
class JwtProviderTest {

    private final UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);

    private final TokenProvider tokenProvider = new JwtProvider(userDetailsService);

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        String key = "c2VjcmV0S2V5U3RyaW5nc2VjcmV0S2V5U3RyaW5nc2VjcmV0S2V5U3RyaW5nc2VjcmV0S2V5U3RyaW5n";  // Base64로 인코딩된 문자열
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        ReflectionTestUtils.setField(tokenProvider, "issuer", "issuer");
        ReflectionTestUtils.setField(tokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenProvider, "secretKeyString", key);
        tokenProvider.init();  // init 메서드를 필드 설정 후에 호출
    }

    @Test
    @DisplayName("JwtProvider 객체 생성")
    void testIssueAccessToken() {
        String token = tokenProvider.issueAccessToken(
                1L,
                "test@example.com",
                UserRole.ROLE_USER,
                Country.SOUTH_KOREA
        );
        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("1", claims.getSubject());
        assertEquals("test@example.com", claims.get("email"));
        assertEquals("ROLE_USER", claims.get("userRole"));
        assertEquals("KR", claims.get("country"));
    }

    @Test
    @DisplayName("Refresh Token 발급")
    void testIssueRefreshToken() {
        String token = tokenProvider.issueRefreshToken();
        assertNotNull(token);

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        assertEquals("issuer", claimsJws.getBody().getIssuer());
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void testValidateToken_Valid() {
        String token = tokenProvider.issueAccessToken(
                1L,
                "test@example.com",
                UserRole.ROLE_USER,
                Country.SOUTH_KOREA
        );

        boolean isValid = tokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("토큰 유효성 검사 - 유효하지 않은 토큰")
    void testValidateToken_Invalid() {
        String invalidToken = "invalid.token.value";
        boolean isValid = tokenProvider.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("토큰에서 ID 추출")
    void testGetIdFromToken() {
        String token = tokenProvider.issueAccessToken(
                1L,
                "test@example.com",
                UserRole.ROLE_USER,
                Country.SOUTH_KOREA
        );

        Long id = tokenProvider.getIdFromToken(token);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("토큰에서 이메일 추출")
    void testGetEmailFromToken() {
        String token = tokenProvider.issueAccessToken(
                1L,
                "test@example.com",
                UserRole.ROLE_USER,
                Country.SOUTH_KOREA
        );

        String email = tokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("Authentication 객체 조회")
    void testGetAuthentication() {
        String token = tokenProvider.issueAccessToken(
                1L,
                "test@example.com",
                UserRole.ROLE_USER,
                Country.SOUTH_KOREA
        );

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("test@example.com"))
                .thenReturn(mockUserDetails);

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(mockUserDetails, authentication.getPrincipal());
        verify(userDetailsService).loadUserByUsername("test@example.com");
    }
}
