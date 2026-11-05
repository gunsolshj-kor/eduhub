package com.example.eduhub.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.eduhub.course.dto.CourseCreateRequest;
import com.example.eduhub.course.dto.CourseUpdateRequest;
import com.example.eduhub.course.dto.SessionCreateRequest;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * CourseService 단위 테스트 (FR-03 과정·회차 관리).
 * DB 없이 Mockito로 리포지토리를 대신해, 서비스와 엔티티의 업무 규칙만 빠르게 검증한다.
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseSessionRepository courseSessionRepository;

    @InjectMocks
    CourseService courseService;

    // ---------- 테스트 데이터 ----------

    private static Course openCourse(Long id) {
        Course course = Course.create("Spring Boot 입문", "BACKEND", "스프링 부트 기초");
        ReflectionTestUtils.setField(course, "id", id); // DB가 없으므로 id를 직접 넣는다
        return course;
    }

    private static Course closedCourse(Long id) {
        Course course = openCourse(id);
        course.close();
        return course;
    }

    @Nested
    @DisplayName("과정 등록")
    class Create {

        @Test
        @DisplayName("요청 내용으로 OPEN 상태의 과정을 저장하고 id를 돌려준다")
        void create_savesOpenCourse() {
            // given
            var request = new CourseCreateRequest("JPA 실전", "BACKEND", "연관관계와 성능");
            given(courseRepository.save(any(Course.class))).willAnswer(invocation -> {
                Course saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 1L);
                return saved;
            });

            // when
            Long id = courseService.create(request);

            // then
            assertThat(id).isEqualTo(1L);

            ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
            then(courseRepository).should().save(captor.capture());
            assertThat(captor.getValue())
                    .extracting(Course::getTitle, Course::getCategory, Course::getDescription, Course::getStatus)
                    .containsExactly("JPA 실전", "BACKEND", "연관관계와 성능", CourseStatus.OPEN);
        }
    }

    @Nested
    @DisplayName("과정 수정")
    class Update {

        @Test
        @DisplayName("열린 과정은 제목·분류·설명을 수정한다")
        void update_openCourse_changesFields() {
            // given
            Course course = openCourse(1L);
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));

            // when
            courseService.update(1L, new CourseUpdateRequest("Spring Boot 심화", "BACKEND", "운영과 성능"));

            // then
            assertThat(course.getTitle()).isEqualTo("Spring Boot 심화");
            assertThat(course.getDescription()).isEqualTo("운영과 성능");
            // 변경 감지(dirty checking)로 반영되므로 save를 다시 부르지 않는다
            then(courseRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("종료된 과정은 수정할 수 없다 (FR-03)")
        void update_closedCourse_throwsCourseClosedException() {
            // given
            Course course = closedCourse(1L);
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));
            var request = new CourseUpdateRequest("바뀐 제목", "BACKEND", "바뀐 설명");

            // when & then
            assertThatThrownBy(() -> courseService.update(1L, request))
                    .isInstanceOf(CourseClosedException.class)
                    .hasMessageContaining("id=1");

            // 예외가 나면 값이 하나도 바뀌지 않아야 한다
            assertThat(course.getTitle()).isEqualTo("Spring Boot 입문");
        }

        @Test
        @DisplayName("없는 과정을 수정하면 CourseNotFoundException")
        void update_missingCourse_throwsNotFound() {
            given(courseRepository.findById(99L)).willReturn(Optional.empty());
            var request = new CourseUpdateRequest("제목", "BACKEND", "설명");

            assertThatThrownBy(() -> courseService.update(99L, request)).isInstanceOf(CourseNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("회차 추가")
    class AddSession {

        private final LocalDate startDate = LocalDate.of(2026, 11, 2);

        @Test
        @DisplayName("열린 과정에 회차를 추가하고 회차 id를 돌려준다")
        void addSession_openCourse_savesSession() {
            // given
            Course course = openCourse(1L);
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));
            given(courseSessionRepository.save(any(CourseSession.class))).willAnswer(invocation -> {
                CourseSession saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 10L);
                return saved;
            });

            // when
            Long sessionId = courseService.addSession(1L, new SessionCreateRequest(startDate, "본사 3층 교육장", 20));

            // then
            assertThat(sessionId).isEqualTo(10L);

            ArgumentCaptor<CourseSession> captor = ArgumentCaptor.forClass(CourseSession.class);
            then(courseSessionRepository).should().save(captor.capture());
            CourseSession session = captor.getValue();
            assertThat(session.getCourse()).isSameAs(course);
            assertThat(session.getStartDate()).isEqualTo(startDate);
            assertThat(session.getPlace()).isEqualTo("본사 3층 교육장");
            assertThat(session.getCapacity()).isEqualTo(20);
            assertThat(session.getStatus()).isEqualTo(SessionStatus.OPEN);
            assertThat(course.getSessions()).containsExactly(session);
        }

        @Test
        @DisplayName("종료된 과정에는 회차를 추가할 수 없다")
        void addSession_closedCourse_throwsCourseClosedException() {
            given(courseRepository.findById(1L)).willReturn(Optional.of(closedCourse(1L)));
            var request = new SessionCreateRequest(startDate, "본사 3층 교육장", 20);

            assertThatThrownBy(() -> courseService.addSession(1L, request)).isInstanceOf(CourseClosedException.class);
            then(courseSessionRepository).should(never()).save(any());
        }

        @ParameterizedTest(name = "정원 {0}명")
        @ValueSource(ints = {0, -1})
        @DisplayName("정원이 1명보다 적으면 회차를 만들 수 없다")
        void addSession_invalidCapacity_throwsIllegalArgument(int capacity) {
            given(courseRepository.findById(1L)).willReturn(Optional.of(openCourse(1L)));
            var request = new SessionCreateRequest(startDate, "본사 3층 교육장", capacity);

            assertThatThrownBy(() -> courseService.addSession(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정원");
            then(courseSessionRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("과정 종료")
    class Close {

        @Test
        @DisplayName("과정을 종료하면 상태가 CLOSED가 된다")
        void close_changesStatusToClosed() {
            Course course = openCourse(1L);
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));

            courseService.close(1L);

            assertThat(course.getStatus()).isEqualTo(CourseStatus.CLOSED);
        }

        @Test
        @DisplayName("이미 종료된 과정을 다시 종료해도 예외 없이 CLOSED를 유지한다")
        void close_alreadyClosed_isIdempotent() {
            Course course = closedCourse(1L);
            given(courseRepository.findById(1L)).willReturn(Optional.of(course));

            courseService.close(1L);

            assertThat(course.getStatus()).isEqualTo(CourseStatus.CLOSED);
        }

        @Test
        @DisplayName("없는 과정을 종료하면 CourseNotFoundException")
        void close_missingCourse_throwsNotFound() {
            given(courseRepository.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> courseService.close(99L)).isInstanceOf(CourseNotFoundException.class);
        }
    }
}
