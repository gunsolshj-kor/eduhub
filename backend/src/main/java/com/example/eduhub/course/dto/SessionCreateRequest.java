package com.example.eduhub.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SessionCreateRequest(
        @NotNull LocalDate startDate,
        @NotBlank @Size(max = 100) String place,
        @Min(value = 1, message = "정원은 1명 이상이어야 합니다") int capacity) {}
