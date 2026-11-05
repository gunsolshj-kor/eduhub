package com.example.eduhub.course;

import com.example.eduhub.common.web.PageResponse;
import com.example.eduhub.course.dto.CourseDetailResponse;
import com.example.eduhub.course.dto.CourseSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** 과정 조회 (FR-01, FR-02). */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseQueryService {

    private final CourseRepository courseRepository;

    public PageResponse<CourseSummaryResponse> search(String keyword, String category, Pageable pageable) {
        return PageResponse.from(courseRepository.search(blankToNull(keyword), blankToNull(category), pageable));
    }

    public CourseDetailResponse get(Long id) {
        return courseRepository
                .findWithSessionsById(id)
                .map(CourseDetailResponse::from)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    private static String blankToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
