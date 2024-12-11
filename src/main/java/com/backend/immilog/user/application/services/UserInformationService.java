package com.backend.immilog.user.application.services;

import com.backend.immilog.image.application.service.ImageService;
import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.backend.immilog.global.enums.UserRole.ROLE_ADMIN;
import static com.backend.immilog.user.exception.UserErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInformationService {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public void updateInformation(
            Long userSeq,
            CompletableFuture<Pair<String, String>> country,
            UserInfoUpdateCommand userInfoUpdateCommand
    ) {
        User user = getUser(userSeq);
        changeCountryIfItsChanged(country, userInfoUpdateCommand, user);
        changeImageProfileIfItsChanged(userInfoUpdateCommand.profileImage(), user);
        user.changeNickName(userInfoUpdateCommand.nickName());
        user.changeInterestCountry(userInfoUpdateCommand.interestCountry());
        user.changeUserStatus(userInfoUpdateCommand.status());
        userCommandService.save(user);
    }

    public void changePassword(
            Long userSeq,
            UserPasswordChangeCommand userPasswordChangeRequest
    ) {
        User user = getUser(userSeq);
        final String existingPassword = userPasswordChangeRequest.existingPassword();
        final String newPassword = userPasswordChangeRequest.newPassword();
        final String currentPassword = user.getPassword();
        throwExceptionIfPasswordNotMatch(existingPassword, currentPassword);
        user.changePassword(passwordEncoder.encode(newPassword));
        userCommandService.save(user);
    }

    public void blockOrUnblockUser(
            Long userSeq,
            Long adminSeq,
            UserStatus userStatus
    ) {
        validateAdminUser(adminSeq);
        User user = getUser(userSeq);
        user.changeUserStatus(userStatus);
        userCommandService.save(user);
    }

    private User getUser(
            Long userSeq
    ) {
        return userQueryService
                .getUserById(userSeq)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private void validateAdminUser(
            Long userSeq
    ) {
        if (!getUser(userSeq).getUserRole().equals(ROLE_ADMIN)) {
            throw new UserException(NOT_AN_ADMIN_USER);
        }
    }

    private void throwExceptionIfPasswordNotMatch(
            String existingPassword,
            String currentPassword
    ) {
        if (!passwordEncoder.matches(existingPassword, currentPassword)) {
            throw new UserException(PASSWORD_NOT_MATCH);
        }
    }

    private void changeCountryIfItsChanged(
            CompletableFuture<Pair<String, String>> country,
            UserInfoUpdateCommand userInfoUpdateCommand,
            User user
    ) {
        try {
            if (userInfoUpdateCommand.country() != null && country.get() != null) {
                Pair<String, String> countryPair = country.join();
                user.changeRegion(countryPair.getSecond());
                user.changeCountry(userInfoUpdateCommand.country());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.info(e.getMessage());
        }
    }

    private void changeImageProfileIfItsChanged(
            String profileUrl,
            User user
    ) {
        if (profileUrl != null) {
            imageService.deleteFile(user.getImageUrl());
            user.changeImageUrl(profileUrl.isEmpty() ? null : profileUrl);
        }
    }

}