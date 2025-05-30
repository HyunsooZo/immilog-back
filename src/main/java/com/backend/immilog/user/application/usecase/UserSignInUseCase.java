package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserSignInCommand;
import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.application.result.UserSignInResult;
import org.springframework.data.util.Pair;

import java.util.concurrent.CompletableFuture;

public interface UserSignInUseCase {
    UserSignInResult signIn(
            UserSignInCommand command,
            CompletableFuture<LocationResult> country
    );

    UserSignInResult getUserSignInDTO(
            Long userSeq,
            LocationResult country
    );
}
