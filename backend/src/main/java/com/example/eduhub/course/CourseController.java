package com.example.eduhub.course;

import com.example.eduhub.common.error.ErrorResponse;
import com.example.eduhub.common.web.PageResponse;
import com.example.eduhub.course.dto.CourseDetailResponse;
import com.example.eduhub.course.dto.CourseSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Course", description = "교육과정 조회·관리")
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseQueryService courseQueryService;

    @Operation(summary = "과정 목록 조회", description = "제목·분류로 검색하고 페이지 단위로 조회한다 (page는 0부터)")
    @GetMapping
    public PageResponse<CourseSummaryResponse> getCourses(
            @Parameter(description = "제목 검색어 (부분 일치)") @RequestParam(required = false) String keyword,
            @Parameter(description = "분류 코드 (예: BACKEND)") @RequestParam(required = false) String category,
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return courseQueryService.search(keyword, category, pageable);
    }

    @Operation(summary = "과정 상세 조회", description = "과정 정보와 회차 목록(일정, 장소, 정원, 남은 자리)을 조회한다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(
            responseCode = "404",
            description = "과정이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public CourseDetailResponse getCourse(@PathVariable Long id) {
        return courseQueryService.get(id);
    }
}
