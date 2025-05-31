package com.backend.immilog.user.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.application.result.EmailVerificationResult;
import com.backend.immilog.user.application.result.UserNickNameResult;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.domain.model.user.Auth;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.service.UserPasswordPolicy;
import com.backend.immilog.user.domain.service.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface UserSignUpUseCase {
    UserNickNameResult signUp(UserSignUpCommand command);

    Boolean isNicknameAvailable(String nickname);

    EmailVerificationResult verifyEmail(Long userSeq);

    @Slf4j
    @Service
    class UserSignUpProcessor implements UserSignUpUseCase {
        private final UserQueryService userQueryService;
        private final UserCommandService userCommandService;
        private final UserPasswordPolicy userPasswordPolicy;
        private final UserValidator userValidator;

        public UserSignUpProcessor(
                UserQueryService userQueryService,
                UserCommandService userCommandService,
                UserPasswordPolicy userPasswordPolicy,
                UserValidator userValidator
        ) {
            this.userQueryService = userQueryService;
            this.userCommandService = userCommandService;
            this.userPasswordPolicy = userPasswordPolicy;
            this.userValidator = userValidator;
        }

        @Override
        public UserNickNameResult signUp(UserSignUpCommand command) {
            userValidator.isExistingUser(command.email());
            var user = createUser(command);
            var savedUser = userCommandService.save(user);
            return new UserNickNameResult(savedUser.seq(), savedUser.nickname());
        }

        @Override
        public Boolean isNicknameAvailable(String nickname) {
            return userQueryService.isNicknameAvailable(nickname);
        }

        @Override
        public EmailVerificationResult verifyEmail(Long userSeq) {
            final var user = userQueryService.getUserById(userSeq);
            final var isKoreanUser = userValidator.isKorean(user.country());
            return userValidator.getVerificationResult(user.userStatus(), isKoreanUser);
        }

        private User createUser(UserSignUpCommand command) {
            final var password = userPasswordPolicy.encode(command.password());
            final var country = Country.valueOf(command.country());
            final var interestCountryValue = command.interestCountry();
            final var isInterestCountryNull = interestCountryValue == null || interestCountryValue.isEmpty();
            final var interestCountry = isInterestCountryNull ? null : country;
            var auth = Auth.of(command.email(), password);
            var location = Location.of(country, command.region());
            var profile = Profile.of(command.nickName(), command.profileImage(), interestCountry);
            return User.of(auth, location, profile);
        }
    }
}
