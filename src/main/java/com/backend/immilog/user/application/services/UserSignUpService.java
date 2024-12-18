package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.domain.enums.UserStatus.ACTIVE;
import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;
import static com.backend.immilog.user.exception.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
public class UserSignUpService {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpService(
            UserQueryService userQueryService,
            UserCommandService userCommandService,
            PasswordEncoder passwordEncoder
    ) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.passwordEncoder = passwordEncoder;
    }

    public Pair<Long, String> signUp(UserSignUpCommand userSignUpCommand) {
        validateUserNotExists(userSignUpCommand.email());
        String password = passwordEncoder.encode(userSignUpCommand.password());
        User user = userCommandService.save(User.of(userSignUpCommand, password));
        return Pair.of(user.getSeq(), user.getNickName());
    }

    public Boolean checkNickname(String nickname) {
        return userQueryService.getUserByNickname(nickname).isEmpty();
    }

    public Pair<String, Boolean> verifyEmail(Long userSeq) {
        User user = userQueryService.getUserById(userSeq).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        String resultString = "이메일 인증이 완료되었습니다.";
        UserStatus currentUserStatus = user.getUserStatus();
        return getVerificationResult(currentUserStatus, user, resultString);
    }

    private void validateUserNotExists(String email) {
        userQueryService
                .getUserByEmail(email)
                .ifPresent(user -> {
                    throw new UserException(EXISTING_USER);
                });
    }

    private Pair<String, Boolean> getVerificationResult(
            UserStatus userStatus,
            User user,
            String resultString
    ) {
        boolean isLoginAvailable = true;
        switch (userStatus) {
            case ACTIVE -> resultString = "이미 인증된 사용자입니다.";
            case PENDING -> {
                user.changeUserStatus(ACTIVE);
                userCommandService.save(user);
            }
            case BLOCKED -> {
                resultString = "차단된 사용자입니다.";
                isLoginAvailable = false;
            }
            default -> resultString = "이메일 인증이 필요한 사용자가 아닙니다.";
        }
        return Pair.of(resultString, isLoginAvailable);
    }
}

