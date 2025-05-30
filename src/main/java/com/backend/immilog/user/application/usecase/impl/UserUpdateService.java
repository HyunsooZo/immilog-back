package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.image.application.ImageUploader;
import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.application.usecase.UserUpdateUseCase;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.domain.service.UserValidator;
import com.backend.immilog.user.domain.service.UserPasswordPolicy;
import lombok.extern.slf4j.Slf4j;
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
    private final UserPasswordPolicy userPasswordPolicy;

    public UserUpdateService(
            UserQueryService userQueryService,
            UserCommandService userCommandService,
            PasswordEncoder passwordEncoder,
            ImageUploader imageUploader,
            UserPasswordPolicy userPasswordPolicy
    ) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.passwordEncoder = passwordEncoder;
        this.imageUploader = imageUploader;
        this.userPasswordPolicy = userPasswordPolicy;
    }

    @Override
    public void updateInformation(
            Long userSeq,
            CompletableFuture<LocationResult> futureRegion,
            UserInfoUpdateCommand userInfoUpdateCommand
    ) {
        var user = userQueryService.getUserById(userSeq);
        var previousProfileImage = user.imageUrl();
        var region = getRegion(futureRegion);
        var updatedUser = user
                .updateNickname(userInfoUpdateCommand.nickName())
                .updateRegion(region)
                .updateCountry(userInfoUpdateCommand.country())
                .updateInterestCountry(userInfoUpdateCommand.interestCountry())
                .updateStatus(userInfoUpdateCommand.status())
                .updateImageUrl(userInfoUpdateCommand.profileImage());
        userCommandService.save(updatedUser);
        imageUploader.deleteFile(previousProfileImage, userInfoUpdateCommand.profileImage());
    }

    @Override
    public void changePassword(
            Long userSeq,
            UserPasswordChangeCommand command
    ) {
        var user = userQueryService.getUserById(userSeq);
        final var existingPassword = command.existingPassword();
        final var newPassword = command.newPassword();
        final var currentPassword = user.password();
        userPasswordPolicy.validate(existingPassword, currentPassword);
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
        userQueryService.getUserById(adminSeq).validateAdmin();
        var targetUser = userQueryService.getUserById(targetUserSeq).updateStatus( UserStatus.valueOf(userStatus));
        userCommandService.save(targetUser);
    }

    private String getRegion(CompletableFuture<LocationResult> country) {
        try {
            if (country.get() != null) {
                var countryPair = country.join();
                return countryPair.city();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}