package com.backend.immilog.user.presentation.request;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.command.UserInfoUpdateCommand;
import com.backend.immilog.user.domain.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

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
