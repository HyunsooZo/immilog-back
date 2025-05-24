package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import org.springframework.data.util.Pair;

import java.util.concurrent.CompletableFuture;

public interface UserUpdateUseCase {
    void updateInformation(
            Long userSeq,
            CompletableFuture<Pair<String, String>> futureRegion,
            UserInfoUpdateCommand userInfoUpdateCommand
    );

    void changePassword(
            Long userSeq,
            UserPasswordChangeCommand command
    );

    void blockOrUnblockUser(
            Long targetUserSeq,
            Long adminSeq,
            String userStatus
    );
}
