package com.backend.immilog.user.presentation.payload;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import com.backend.immilog.user.application.command.UserReportCommand;
import com.backend.immilog.user.domain.model.report.ReportReason;
import com.backend.immilog.user.domain.model.user.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInformationPayload() {
    @Schema(description = "사용자 정보 수정 요청 DTO")
    public record UserInfoUpdateRequest(
            @Schema(description = "닉네임", example = "닉네임") String nickName,
            @Schema(description = "프로필 이미지", example = "프로필 이미지") String profileImage,
            @Schema(description = "국가", example = "SOUTH_KOREA") Country country,
            @Schema(description = "관심 국가", example = "MALAYSIA") Country interestCountry,
            @Schema(description = "위도", example = "37.123456") Double latitude,
            @Schema(description = "경도", example = "127.123456") Double longitude,
            @Schema(description = "사용자 상태", example = "ACTIVE") UserStatus status
    ) {
        public UserInfoUpdateCommand toCommand() {
            return new UserInfoUpdateCommand(
                    this.nickName,
                    this.profileImage,
                    this.country,
                    this.interestCountry,
                    this.latitude,
                    this.longitude,
                    this.status
            );
        }
    }

    public record UserNicknameResponse(
            @Schema(description = "상태 코드", example = "200") Integer status,
            @Schema(description = "메시지", example = "success") String message,
            @Schema(description = "닉네임 중복 여부", example = "true") Boolean data
    ) {
        public UserNicknameResponse(Boolean nicknameExist) {
            this(
                    200,
                    nicknameExist ? "success" : "fail",
                    nicknameExist
            );
        }
    }

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

    @Schema(description = "사용자 신고 요청 DTO")
    public record UserReportRequest(
            @Schema(description = "신고 사유", example = "SPAM") ReportReason reason,
            @Schema(description = "신고 내용", example = "신고 내용") String description
    ) {
        public UserReportCommand toCommand() {
            return new UserReportCommand(
                    this.reason,
                    this.description
            );
        }
    }
}
