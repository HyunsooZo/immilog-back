package com.backend.immilog.user.application.usecase;

import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface LocationFetchUseCase {
    @Async
    CompletableFuture<Pair<String, String>> getCountry(
            Double latitude,
            Double longitude
    );

    Pair<String, String> joinCompletableFutureLocation(CompletableFuture<Pair<String, String>> countryFuture);
}
