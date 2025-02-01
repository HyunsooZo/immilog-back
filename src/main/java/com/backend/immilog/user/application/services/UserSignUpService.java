package com.backend.immilog.user.application.services;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Auth;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.domain.enums.UserStatus.ACTIVE;
import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;

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

    public Pair<Long, String> signUp(UserSignUpCommand command) {
        validateExistingUser(command.email());
        User user = createUser(command);
        User savedUser = userCommandService.save(user);
        return Pair.of(savedUser.seq(), savedUser.nickname());
    }

    public Boolean isNicknameAvailable(String nickname) {
        return userQueryService.isNicknameAvailable(nickname);
    }

    public Pair<String, Boolean> verifyEmail(Long userSeq) {
        User user = userQueryService.getUserById(userSeq);
        String resultString = "이메일 인증이 완료되었습니다.";
        UserStatus currentUserStatus = user.userStatus();
        return getVerificationResult(currentUserStatus, user, resultString);
    }

    private User createUser(UserSignUpCommand command) {
        final String password = passwordEncoder.encode(command.password());
        final Country country = Country.valueOf(command.country());
        final String interestCountryValue = command.interestCountry();
        final boolean isInterestCountryNull = interestCountryValue == null || interestCountryValue.isEmpty();
        final Country interestCountry = isInterestCountryNull ? null : country;
        Auth auth = Auth.of(command.email(), password);
        Location location = Location.of(country, command.region());
        Profile profile = Profile.of(command.nickName(), command.profileImage(), interestCountry);
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
            String resultString
    ) {
        boolean isLoginAvailable = true;
        switch (userStatus) {
            case ACTIVE -> resultString = "이미 인증된 사용자입니다.";
            case PENDING -> {
                user.updateStatus(ACTIVE);
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

