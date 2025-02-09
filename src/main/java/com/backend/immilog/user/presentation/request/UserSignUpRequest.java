package com.backend.immilog.user.presentation.request;

import com.backend.immilog.user.application.command.UserSignUpCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 회원가입 요청 DTO")
public record UserSignUpRequest(
        @Schema(description = "닉네임", example = "닉네임")
        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickName,

        @Schema(description = "비밀번호", example = "password")
        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자여야 합니다.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        @Schema(description = "이메일", example = "email@email.com")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        @Schema(description = "국가", example = "SOUTH_KOREA")
        @NotNull(message = "국가를 입력해주세요.")
        String country,

        @Schema(description = "관심 국가", example = "MALAYSIA")
        String interestCountry,

        @Schema(description = "지역", example = "서울")
        String region,

        @Schema(description = "프로필 이미지", example = "프로필 이미지")
        String profileImage
) {
    public UserSignUpCommand toCommand() {
        return new UserSignUpCommand(
                this.nickName,
                this.password,
                this.email,
                this.country,
                this.interestCountry,
                this.region,
                this.profileImage
        );
    }
}
