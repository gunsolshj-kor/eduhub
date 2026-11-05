package com.example.eduhub.course;

import com.example.eduhub.course.dto.CourseCreateRequest;
import com.example.eduhub.course.dto.CourseUpdateRequest;
import com.example.eduhub.course.dto.SessionCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 과정·회차 관리 (FR-03). 업무 규칙은 엔티티에 있고, 서비스는 조회하고 위임한다. */
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseSessionRepository courseSessionRepository;

    public Long create(CourseCreateRequest request) {
        Course course = Course.create(request.title(), request.category(), request.description());
        return courseRepository.save(course).getId();
    }

    public void update(Long id, CourseUpdateRequest request) {
        Course course = getCourse(id);
        course.update(request.title(), request.category(), request.description()); // 변경 감지로 반영
    }

    public Long addSession(Long courseId, SessionCreateRequest request) {
        Course course = getCourse(courseId);
        CourseSession session = course.addSession(request.startDate(), request.place(), request.capacity());
        return courseSessionRepository.save(session).getId();
    }

    public void close(Long id) {
        getCourse(id).close();
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
}
