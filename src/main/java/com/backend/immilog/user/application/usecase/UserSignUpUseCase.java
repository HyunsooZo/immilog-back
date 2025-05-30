package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.application.result.EmailVerificationResult;
import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.application.result.UserNickNameResult;
import org.springframework.data.util.Pair;

public interface UserSignUpUseCase {
    UserNickNameResult signUp(UserSignUpCommand command);

    Boolean isNicknameAvailable(String nickname);

    EmailVerificationResult verifyEmail(Long userSeq);
}
