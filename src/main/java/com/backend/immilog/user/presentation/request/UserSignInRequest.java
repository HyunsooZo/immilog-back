package com.backend.immilog.user.presentation.request;

import com.backend.immilog.user.application.command.UserSignInCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 로그인 요청 DTO")
public record UserSignInRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자여야 합니다.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        Double latitude,

        Double longitude
) {
    public UserSignInCommand toCommand() {
        return new UserSignInCommand(
                this.email,
                this.password,
                this.latitude,
                this.longitude
        );
    }
}
