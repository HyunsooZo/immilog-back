package com.backend.immilog.user.infrastructure.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeocodeGateway {
    private final GeocodeConfig geocodeConfig;
    private final RestClient restClient;

    public GeocodeGateway(GeocodeConfig geocodeConfig, RestClient restClient) {
        this.geocodeConfig = geocodeConfig;
        this.restClient = restClient;
    }

    public String fetchGeocode(double latitude, double longitude) {
        var url = String.format(geocodeConfig.url(), latitude, longitude, geocodeConfig.key());
        return restClient.get().uri(url).retrieve().body(String.class);
    }
}
