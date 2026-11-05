package com.example.eduhub.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/** API 에러 코드. 클라이언트는 code 값으로 분기한다. */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "과정을 찾을 수 없습니다."),
    COURSE_CLOSED(HttpStatus.CONFLICT, "종료된 과정은 변경할 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
