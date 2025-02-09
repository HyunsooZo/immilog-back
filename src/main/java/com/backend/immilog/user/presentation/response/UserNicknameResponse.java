package com.backend.immilog.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

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
