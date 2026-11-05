package com.example.eduhub.course;

import com.example.eduhub.common.error.ErrorResponse;
import com.example.eduhub.common.web.PageResponse;
import com.example.eduhub.course.dto.CourseCreateRequest;
import com.example.eduhub.course.dto.CourseDetailResponse;
import com.example.eduhub.course.dto.CourseSummaryResponse;
import com.example.eduhub.course.dto.CourseUpdateRequest;
import com.example.eduhub.course.dto.IdResponse;
import com.example.eduhub.course.dto.SessionCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Course", description = "교육과정 조회·관리")
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseQueryService courseQueryService;
    private final CourseService courseService;

    @Operation(summary = "과정 등록", description = "OPEN 상태의 과정을 등록하고 id를 돌려준다")
    @ApiResponse(responseCode = "201", description = "등록 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<IdResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        Long id = courseService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/courses/" + id)).body(new IdResponse(id));
    }

    @Operation(summary = "과정 수정", description = "제목·분류·설명을 수정한다. 종료된 과정은 수정할 수 없다")
    @ApiResponse(responseCode = "204", description = "수정 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(
            responseCode = "404",
            description = "과정이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(
            responseCode = "409",
            description = "종료된 과정",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest request) {
        courseService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회차 추가", description = "과정에 회차를 추가한다. 정원은 1명 이상, 종료된 과정에는 추가할 수 없다")
    @ApiResponse(responseCode = "201", description = "추가 성공")
    @ApiResponse(
            responseCode = "400",
            description = "입력값 오류 (정원 1명 미만 등)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(
            responseCode = "404",
            description = "과정이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(
            responseCode = "409",
            description = "종료된 과정",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{id}/sessions")
    public ResponseEntity<IdResponse> addSession(
            @PathVariable Long id, @Valid @RequestBody SessionCreateRequest request) {
        Long sessionId = courseService.addSession(id, request);
        return ResponseEntity.status(201).body(new IdResponse(sessionId));
    }

    @Operation(summary = "과정 종료", description = "과정을 CLOSED로 바꾼다. 이미 종료된 과정이면 그대로 둔다")
    @ApiResponse(responseCode = "204", description = "종료 성공")
    @ApiResponse(
            responseCode = "404",
            description = "과정이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeCourse(@PathVariable Long id) {
        courseService.close(id);
        return ResponseEntity.noContent().build();
    }

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
