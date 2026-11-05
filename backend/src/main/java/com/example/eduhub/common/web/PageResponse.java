package com.example.eduhub.common.web;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

/** 페이지 응답. page는 0부터 시작한다. 필드는 항상 채워지므로 OpenAPI에 required로 표시한다. */
public record PageResponse<T>(
        @Schema(requiredMode = REQUIRED) List<T> content,
        @Schema(requiredMode = REQUIRED, description = "0부터 시작하는 페이지 번호") int page,
        @Schema(requiredMode = REQUIRED) int size,
        @Schema(requiredMode = REQUIRED) long totalElements) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
