package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.result.LocationResult;
import kotlin.reflect.jvm.internal.impl.incremental.components.LocationInfo;

import java.util.concurrent.CompletableFuture;

public interface LocationFetchUseCase {
    CompletableFuture<LocationResult> getCountry(
            Double latitude,
            Double longitude
    );

    LocationResult joinCompletableFutureLocation(CompletableFuture<LocationResult> countryFuture);
}
