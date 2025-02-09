package com.backend.immilog.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record UserLocationResponse(
        @Schema(description = "상태 코드", example = "200") Integer status,
        @Schema(description = "메시지", example = "success") String message,
        @Schema(description = "사용자 위치 정보") LocationResponse data
) {
    public UserLocationResponse(
            String country,
            String region
    ) {
        this(
                HttpStatus.OK.value(),
                "success",
                new LocationResponse(country, region)
        );
    }

    public record LocationResponse(
            @Schema(description = "국가", example = "SOUTH_KOREA") String country,
            @Schema(description = "지역", example = "서울") String region
    ) {}
}
