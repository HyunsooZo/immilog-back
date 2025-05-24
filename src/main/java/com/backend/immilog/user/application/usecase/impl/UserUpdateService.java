package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.image.application.ImageUploader;
import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.application.usecase.UserUpdateUseCase;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class UserUpdateService implements UserUpdateUseCase {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploader imageUploader;

    public UserUpdateService(
            UserQueryService userQueryService,
            UserCommandService userCommandService,
            PasswordEncoder passwordEncoder,
            ImageUploader imageUploader
    ) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.passwordEncoder = passwordEncoder;
        this.imageUploader = imageUploader;
    }

    @Override
    public void updateInformation(
            Long userSeq,
            CompletableFuture<Pair<String, String>> futureRegion,
            UserInfoUpdateCommand userInfoUpdateCommand
    ) {
        var user = getUser(userSeq);
        var previousProfileImage = user.imageUrl();
        var region = getRegion(futureRegion);
        var updatedUser = user.updateNickname(userInfoUpdateCommand.nickName())
                .updateRegion(region)
                .updateCountry(userInfoUpdateCommand.country())
                .updateInterestCountry(userInfoUpdateCommand.interestCountry())
                .updateStatus(userInfoUpdateCommand.status())
                .updateImageUrl(userInfoUpdateCommand.profileImage());
        userCommandService.save(updatedUser);
        this.deletePreviousFile(previousProfileImage, userInfoUpdateCommand.profileImage());
    }

    @Override
    public void changePassword(
            Long userSeq,
            UserPasswordChangeCommand command
    ) {
        var user = this.getUser(userSeq);
        final String existingPassword = command.existingPassword();
        final String newPassword = command.newPassword();
        final String currentPassword = user.password();
        this.validatePassword(existingPassword, currentPassword);
        var encodedPassword = passwordEncoder.encode(newPassword);
        var updatedUser = user.changePassword(encodedPassword);
        userCommandService.save(updatedUser);
    }

    @Override
    public void blockOrUnblockUser(
            Long targetUserSeq,
            Long adminSeq,
            String userStatus
    ) {
        var userStatusEnum = UserStatus.valueOf(userStatus);
        var targetUser = getUser(targetUserSeq);
        var admin = getUser(adminSeq);
        admin.validateAdmin();
        targetUser.updateStatus(userStatusEnum);
        userCommandService.save(targetUser);
    }

    private User getUser(Long userSeq) {
        return userQueryService.getUserById(userSeq);
    }

    private void validatePassword(
            String existingPassword,
            String currentPassword
    ) {
        if (!passwordEncoder.matches(existingPassword, currentPassword)) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private String getRegion(CompletableFuture<Pair<String, String>> country) {
        try {
            if (country.get() != null) {
                Pair<String, String> countryPair = country.join();
                return countryPair.getSecond();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.info(e.getMessage());
        }
        return null;
    }

    private void deletePreviousFile(
            String previousProfileImage,
            String newProfileImage
    ) {
        if (previousProfileImage != null && !previousProfileImage.equals(newProfileImage)) {
            imageUploader.deleteFile(previousProfileImage);
        }
    }
}