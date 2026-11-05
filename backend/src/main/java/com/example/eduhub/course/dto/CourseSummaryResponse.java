package com.example.eduhub.course.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.eduhub.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/** 과정 목록의 한 행 (FR-01). sessionCount는 목록 쿼리 안에서 집계한다. */
public record CourseSummaryResponse(
        @Schema(requiredMode = REQUIRED) Long id,
        @Schema(requiredMode = REQUIRED) String title,
        @Schema(requiredMode = REQUIRED) String category,
        @Schema(requiredMode = REQUIRED) CourseStatus status,
        @Schema(requiredMode = REQUIRED) long sessionCount) {}
