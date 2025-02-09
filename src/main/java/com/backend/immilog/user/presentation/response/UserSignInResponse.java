package com.backend.immilog.user.presentation.response;

import com.backend.immilog.user.application.result.UserSignInResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignInResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "사용자 로그인 정보") UserSignInResult data
) {}
