package com.backend.immilog.user.presentation.response;

import com.backend.immilog.user.application.result.CompanyResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserCompanyResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "회사 정보", example = "company info") CompanyResult data
) {
}

