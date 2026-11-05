package com.example.eduhub.course.dto;

import com.example.eduhub.course.CourseStatus;

/** 과정 목록의 한 행 (FR-01). sessionCount는 목록 쿼리 안에서 집계한다. */
public record CourseSummaryResponse(Long id, String title, String category, CourseStatus status, long sessionCount) {}
