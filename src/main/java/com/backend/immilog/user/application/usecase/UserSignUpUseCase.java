package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserSignUpCommand;
import org.springframework.data.util.Pair;

public interface UserSignUpUseCase {
    Pair<Long, String> signUp(UserSignUpCommand command);

    Boolean isNicknameAvailable(String nickname);

    Pair<String, Boolean> verifyEmail(Long userSeq);
}
