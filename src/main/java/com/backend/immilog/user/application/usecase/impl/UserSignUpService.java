package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.application.usecase.UserSignUpUseCase;
import com.backend.immilog.user.domain.model.user.*;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.domain.model.user.UserStatus.ACTIVE;
import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;

@Slf4j
@Service
public class UserSignUpService implements UserSignUpUseCase {
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

    @Override
    public Pair<Long, String> signUp(UserSignUpCommand command) {
        validateExistingUser(command.email());
        User user = createUser(command);
        User savedUser = userCommandService.save(user);
        return Pair.of(savedUser.seq(), savedUser.nickname());
    }

    @Override
    public Boolean isNicknameAvailable(String nickname) {
        return userQueryService.isNicknameAvailable(nickname);
    }

    @Override
    public Pair<String, Boolean> verifyEmail(Long userSeq) {
        final var user = userQueryService.getUserById(userSeq);
        final var isKoreanUser = user.country().equals(Country.SOUTH_KOREA);
        final var currentUserStatus = user.userStatus();
        return this.getVerificationResult(currentUserStatus, user, isKoreanUser);
    }

    private User createUser(UserSignUpCommand command) {
        final var password = passwordEncoder.encode(command.password());
        final var country = Country.valueOf(command.country());
        final var interestCountryValue = command.interestCountry();
        final var isInterestCountryNull = interestCountryValue == null || interestCountryValue.isEmpty();
        final var interestCountry = isInterestCountryNull ? null : country;
        var auth = Auth.of(command.email(), password);
        var location = Location.of(country, command.region());
        var profile = Profile.of(command.nickName(), command.profileImage(), interestCountry);
        return User.of(auth, location, profile);
    }

    private void validateExistingUser(String email) {
        if (userQueryService.isUserAlreadyExist(email)) {
            throw new UserException(EXISTING_USER);
        }
    }

    private Pair<String, Boolean> getVerificationResult(
            UserStatus userStatus,
            User user,
            Boolean isKoreanUser
    ) {
        var isLoginAvailable = true;
        var resultString = "";
        switch (userStatus) {
            case ACTIVE -> {
                log.info("User is already verified.");
                resultString = isKoreanUser ? "이미 인증된 사용자입니다." : "User is already verified.";
            }
            case PENDING -> {
                log.info("User is pending verification.");
                resultString = isKoreanUser ? "이메일 인증이 완료되었습니다." : "Email verification is complete.";
                User updatedUser = user.updateStatus(ACTIVE);
                userCommandService.save(updatedUser);
            }
            case BLOCKED -> {
                log.info("User is blocked.");
                resultString = isKoreanUser ? "차단된 사용자입니다." : "Blocked user.";
                isLoginAvailable = false;
            }
            default -> {
                resultString = isKoreanUser ? "이메일 인증이 필요한 사용자가 아닙니다." : "User does not need email verification.";
            }
        }
        return Pair.of(resultString, isLoginAvailable);
    }
}

