package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserSignInCommand;
import com.backend.immilog.user.application.result.UserSignInResult;
import org.springframework.data.util.Pair;

import java.util.concurrent.CompletableFuture;

public interface UserSignInUseCase {
    UserSignInResult signIn(
            UserSignInCommand command,
            CompletableFuture<Pair<String, String>> country
    );

    UserSignInResult getUserSignInDTO(
            Long userSeq,
            Pair<String, String> country
    );
}
