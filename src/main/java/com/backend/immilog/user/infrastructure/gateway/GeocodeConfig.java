package com.backend.immilog.user.infrastructure.gateway;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Accessors(fluent = true)
@ConfigurationProperties(prefix = "geocode")
public class GeocodeConfig {
    private String url;
    private String key;

    public GeocodeConfig(
            String url,
            String key
    ) {
        this.url = url;
        this.key = key;
    }

    public GeocodeConfig() {}
}