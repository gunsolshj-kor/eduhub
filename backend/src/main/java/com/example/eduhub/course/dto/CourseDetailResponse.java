package com.example.eduhub.course.dto;

import com.example.eduhub.course.Course;
import com.example.eduhub.course.CourseStatus;
import java.util.List;

/** 과정 상세와 회차 목록 (FR-02). */
public record CourseDetailResponse(
        Long id,
        String title,
        String category,
        String description,
        CourseStatus status,
        List<SessionResponse> sessions) {

    public static CourseDetailResponse from(Course course) {
        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getCategory(),
                course.getDescription(),
                course.getStatus(),
                course.getSessions().stream().map(SessionResponse::from).toList());
    }
}
