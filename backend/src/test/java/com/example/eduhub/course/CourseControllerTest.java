package com.example.eduhub.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.eduhub.common.web.PageResponse;
import com.example.eduhub.course.dto.CourseDetailResponse;
import com.example.eduhub.course.dto.CourseSummaryResponse;
import com.example.eduhub.course.dto.SessionResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CourseQueryService courseQueryService;

    @MockitoBean
    CourseService courseService;

    @Test
    @DisplayName("검색 조건과 0부터 시작하는 page를 그대로 서비스에 넘기고 PageResponse를 돌려준다")
    void getCourses_withConditions_returnsPage() throws Exception {
        var row = new CourseSummaryResponse(1L, "Spring Boot 입문", "BACKEND", CourseStatus.OPEN, 2);
        given(courseQueryService.search(eq("spring"), eq("BACKEND"), any(Pageable.class)))
                .willReturn(new PageResponse<>(List.of(row), 0, 10, 1));

        mockMvc.perform(get("/api/v1/courses")
                        .param("keyword", "spring")
                        .param("category", "BACKEND")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Spring Boot 입문"))
                .andExpect(jsonPath("$.content[0].sessionCount").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(1));

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        then(courseQueryService).should().search(eq("spring"), eq("BACKEND"), pageable.capture());
        org.assertj.core.api.Assertions.assertThat(pageable.getValue().getPageNumber())
                .isZero();
    }

    @Test
    @DisplayName("조건 없이 조회하면 기본 페이지 크기 10으로 조회한다")
    void getCourses_noConditions_usesDefaultPage() throws Exception {
        given(courseQueryService.search(isNull(), isNull(), any(Pageable.class)))
                .willReturn(new PageResponse<>(List.of(), 0, 10, 0));

        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("과정 상세와 회차 목록을 돌려준다")
    void getCourse_existing_returnsDetail() throws Exception {
        var session = new SessionResponse(10L, LocalDate.of(2026, 11, 2), "본사 3층 교육장", 20, 20, SessionStatus.OPEN);
        given(courseQueryService.get(1L))
                .willReturn(new CourseDetailResponse(
                        1L, "Spring Boot 입문", "BACKEND", "스프링 부트 기초", CourseStatus.OPEN, List.of(session)));

        mockMvc.perform(get("/api/v1/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot 입문"))
                .andExpect(jsonPath("$.sessions[0].startDate").value("2026-11-02"))
                .andExpect(jsonPath("$.sessions[0].remainingSeats").value(20));
    }

    @Test
    @DisplayName("없는 과정을 조회하면 404와 에러 코드를 돌려준다")
    void getCourse_notFound_returns404() throws Exception {
        given(courseQueryService.get(99L)).willThrow(new CourseNotFoundException(99L));

        mockMvc.perform(get("/api/v1/courses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("COURSE_NOT_FOUND"));
    }
}
