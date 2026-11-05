package com.example.eduhub.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.eduhub.course.dto.CourseCreateRequest;
import com.example.eduhub.course.dto.CourseUpdateRequest;
import com.example.eduhub.course.dto.SessionCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** 과정·회차 관리 API(FR-03)의 HTTP 응답(201, 204, 400, 404, 409)을 확인한다. */
@WebMvcTest(CourseController.class)
class CourseAdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CourseQueryService courseQueryService;

    @MockitoBean
    CourseService courseService;

    @Test
    @DisplayName("과정을 등록하면 201과 id, Location 헤더를 돌려준다")
    void createCourse_valid_returns201() throws Exception {
        given(courseService.create(any(CourseCreateRequest.class))).willReturn(1L);

        mockMvc.perform(
                        post("/api/v1/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"title": "JPA 실전", "category": "BACKEND", "description": "연관관계와 성능"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/courses/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("제목이 비어 있으면 400과 INVALID_INPUT을 돌려주고 서비스를 부르지 않는다")
    void createCourse_blankTitle_returns400() throws Exception {
        mockMvc.perform(
                        post("/api/v1/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"title": " ", "category": "BACKEND"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));

        verify(courseService, never()).create(any());
    }

    @Test
    @DisplayName("종료된 과정을 수정하면 409와 COURSE_CLOSED를 돌려준다")
    void updateCourse_closed_returns409() throws Exception {
        willThrow(new CourseClosedException(1L)).given(courseService).update(eq(1L), any(CourseUpdateRequest.class));

        mockMvc.perform(
                        put("/api/v1/courses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"title": "바뀐 제목", "category": "BACKEND"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("COURSE_CLOSED"));
    }

    @Test
    @DisplayName("열린 과정을 수정하면 204를 돌려준다")
    void updateCourse_open_returns204() throws Exception {
        mockMvc.perform(
                        put("/api/v1/courses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"title": "Spring Boot 심화", "category": "BACKEND"}
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회차를 추가하면 201과 회차 id를 돌려준다")
    void addSession_valid_returns201() throws Exception {
        given(courseService.addSession(eq(1L), any(SessionCreateRequest.class))).willReturn(10L);

        mockMvc.perform(
                        post("/api/v1/courses/1/sessions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"startDate": "2026-11-02", "place": "본사 3층 교육장", "capacity": 20}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("정원이 0이면 400을 돌려준다")
    void addSession_zeroCapacity_returns400() throws Exception {
        mockMvc.perform(
                        post("/api/v1/courses/1/sessions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"startDate": "2026-11-02", "place": "본사 3층 교육장", "capacity": 0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    @Test
    @DisplayName("없는 과정을 종료하면 404를 돌려준다")
    void closeCourse_missing_returns404() throws Exception {
        willThrow(new CourseNotFoundException(99L)).given(courseService).close(99L);

        mockMvc.perform(post("/api/v1/courses/99/close"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("COURSE_NOT_FOUND"));
    }

    @Test
    @DisplayName("과정을 종료하면 204를 돌려준다")
    void closeCourse_existing_returns204() throws Exception {
        mockMvc.perform(post("/api/v1/courses/1/close")).andExpect(status().isNoContent());
    }
}
