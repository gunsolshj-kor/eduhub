package com.example.eduhub.common.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 코드", example = "COURSE_NOT_FOUND") String code,
        @Schema(description = "에러 메시지", example = "과정을 찾을 수 없습니다. id=99") String message) {

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.name(), message);
    }
}
