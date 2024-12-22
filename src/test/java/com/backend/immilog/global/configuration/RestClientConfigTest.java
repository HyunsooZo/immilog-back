package com.backend.immilog.global.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("RestClientConfig 클래스")
class RestClientConfigTest {

    private final RestClientConfig restClientConfig = new RestClientConfig();

    @Test
    @DisplayName("RestClient 빈생성")
    void restClientBeanIsCreatedSuccessfully() {
        RestClient restClient = restClientConfig.restClient();
        assertNotNull(restClient);
    }
}