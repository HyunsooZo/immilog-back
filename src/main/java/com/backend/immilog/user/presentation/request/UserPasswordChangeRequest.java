package com.backend.immilog.user.presentation.request;

import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 비밀번호 변경 요청 DTO")
public record UserPasswordChangeRequest(
        @Schema(description = "기존 비밀번호", example = "password")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자여야 합니다.")
        String existingPassword,

        @Schema(description = "새로운 비밀번호", example = "newPassword")
        @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자여야 합니다.")
        String newPassword
) {
    public UserPasswordChangeCommand toCommand() {
        return new UserPasswordChangeCommand(existingPassword, newPassword);
    }
}