package com.backend.immilog.user.domain.service;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.result.EmailVerificationResult;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.backend.immilog.user.exception.UserErrorCode.USER_STATUS_NOT_ACTIVE;

@Slf4j
@Service
public class UserValidator {
    private final UserQueryService userQueryService;

    public UserValidator(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    public void validateUserStatus(UserStatus status) {
        if (!status.equals(UserStatus.ACTIVE)) {
            throw new UserException(USER_STATUS_NOT_ACTIVE);
        }
    }

    public EmailVerificationResult getVerificationResult(
            UserStatus userStatus,
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
        return new EmailVerificationResult(resultString, isLoginAvailable);
    }

    public void isExistingUser(String email) {
        if (userQueryService.isUserExist(email)) {
            throw new UserException(UserErrorCode.EXISTING_USER);
        }
        ;
    }

    public boolean isKorean(Country country) {
        return country.equals(Country.SOUTH_KOREA);
    }
}
