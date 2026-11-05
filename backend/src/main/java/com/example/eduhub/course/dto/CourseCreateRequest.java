package com.example.eduhub.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 50) String category,
        @Size(max = 2000) String description) {}
