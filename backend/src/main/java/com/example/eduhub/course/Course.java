package com.example.eduhub.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 교육과정. 종료(CLOSED)된 과정은 수정하거나 회차를 추가할 수 없다 (FR-03). */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status = CourseStatus.OPEN;

    @OneToMany(mappedBy = "course")
    private List<CourseSession> sessions = new ArrayList<>();

    public static Course create(String title, String category, String description) {
        Course course = new Course();
        course.title = title;
        course.category = category;
        course.description = description;
        return course;
    }

    public void update(String title, String category, String description) {
        checkNotClosed();
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public CourseSession addSession(LocalDate startDate, String place, int capacity) {
        checkNotClosed();
        CourseSession session = CourseSession.create(this, startDate, place, capacity);
        sessions.add(session);
        return session;
    }

    /** 과정을 종료한다. 이미 종료된 과정이면 아무 일도 하지 않는다 (멱등). */
    public void close() {
        this.status = CourseStatus.CLOSED;
    }

    private void checkNotClosed() {
        if (status == CourseStatus.CLOSED) {
            throw new CourseClosedException(id);
        }
    }
}
