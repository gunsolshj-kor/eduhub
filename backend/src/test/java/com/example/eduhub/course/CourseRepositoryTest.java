package com.example.eduhub.course;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/** Flyway V1 스키마 위에서 엔티티 매핑(ddl-auto=validate)과 저장·조회를 확인한다. */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseSessionRepository courseSessionRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("과정과 회차를 저장하고 다시 조회한다")
    void save_courseWithSession_findsById() {
        Course course = courseRepository.save(Course.create("Spring Boot 입문", "BACKEND", "스프링 부트 기초"));
        courseSessionRepository.save(course.addSession(LocalDate.of(2026, 11, 2), "본사 3층 교육장", 20));
        em.flush();
        em.clear();

        Course found = courseRepository.findById(course.getId()).orElseThrow();

        assertThat(found.getTitle()).isEqualTo("Spring Boot 입문");
        assertThat(found.getStatus()).isEqualTo(CourseStatus.OPEN);
        assertThat(found.getSessions()).singleElement().satisfies(session -> {
            assertThat(session.getPlace()).isEqualTo("본사 3층 교육장");
            assertThat(session.getCapacity()).isEqualTo(20);
            assertThat(session.getStatus()).isEqualTo(SessionStatus.OPEN);
        });
    }
}
