package com.backend.immilog.global.configuration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Disabled
@DisplayName("WebConfig 클래스 통합 테스트")
class WebConfigTest {

    private final WebConfig webConfig = new WebConfig();

    @Test
    @DisplayName("사용자 검증을 위한 CORS 매핑 추가 테스트")
    void addCorsMappingsForUserVerification() {
        CorsRegistry corsRegistry = spy(CorsRegistry.class);
        webConfig.addCorsMappings(corsRegistry);
        verify(corsRegistry).addMapping("/api/v1/users/*/verification");
    }

    @Test
    @DisplayName("모든 엔드포인트에 대한 CORS 매핑 추가 테스트")
    void addCorsMappingsForAllEndpoints() {
        CorsRegistry corsRegistry = spy(CorsRegistry.class);
        webConfig.addCorsMappings(corsRegistry);
        verify(corsRegistry).addMapping("/**");
    }
}
