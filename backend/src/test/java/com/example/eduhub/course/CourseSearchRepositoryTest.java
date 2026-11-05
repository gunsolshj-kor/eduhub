package com.example.eduhub.course;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.eduhub.course.dto.CourseSummaryResponse;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

/** 과정 목록 검색 쿼리(FR-01)와 상세 조회(FR-02)를 실제 DB(H2)로 확인한다. */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseSearchRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseSessionRepository courseSessionRepository;

    @Autowired
    EntityManager em;

    private final PageRequest firstPage = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    void setUp() {
        Course spring = courseRepository.save(Course.create("Spring Boot 입문", "BACKEND", null));
        courseSessionRepository.save(spring.addSession(LocalDate.of(2026, 11, 2), "본사 3층", 20));
        courseSessionRepository.save(spring.addSession(LocalDate.of(2026, 12, 7), "본사 3층", 20));
        courseRepository.save(Course.create("JPA 실전", "BACKEND", null));
        courseRepository.save(Course.create("React 기초", "FRONTEND", null));
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("조건이 없으면 전체를 조회하고 회차 수를 함께 센다")
    void search_noConditions_returnsAllWithSessionCount() {
        Page<CourseSummaryResponse> page = courseRepository.search(null, null, firstPage);

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent())
                .extracting(CourseSummaryResponse::title, CourseSummaryResponse::sessionCount)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("Spring Boot 입문", 2L),
                        org.assertj.core.groups.Tuple.tuple("JPA 실전", 0L),
                        org.assertj.core.groups.Tuple.tuple("React 기초", 0L));
    }

    @Test
    @DisplayName("제목은 대소문자 구분 없이 부분 일치로 검색한다")
    void search_keyword_matchesTitleIgnoringCase() {
        Page<CourseSummaryResponse> page = courseRepository.search("spring", null, firstPage);

        assertThat(page.getContent()).extracting(CourseSummaryResponse::title).containsExactly("Spring Boot 입문");
    }

    @Test
    @DisplayName("분류로 거른다")
    void search_category_filters() {
        Page<CourseSummaryResponse> page = courseRepository.search(null, "FRONTEND", firstPage);

        assertThat(page.getContent()).extracting(CourseSummaryResponse::title).containsExactly("React 기초");
    }

    @Test
    @DisplayName("페이지는 0부터 시작하고 전체 건수는 페이지와 무관하다")
    void search_secondPage_returnsRemaining() {
        Page<CourseSummaryResponse> page = courseRepository.search(null, null, PageRequest.of(1, 2, Sort.by("id")));

        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).extracting(CourseSummaryResponse::title).containsExactly("React 기초");
    }

    @Test
    @DisplayName("상세 조회는 회차를 함께 가져온다")
    void findWithSessionsById_loadsSessions() {
        Long id = courseRepository
                .search("spring", null, firstPage)
                .getContent()
                .get(0)
                .id();

        Course course = courseRepository.findWithSessionsById(id).orElseThrow();
        em.clear(); // 영속성 컨텍스트를 비워도 회차가 이미 로딩되어 있어야 한다

        assertThat(course.getSessions()).hasSize(2);
    }
}
