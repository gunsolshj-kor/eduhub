package com.example.eduhub.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 과정의 회차(일정, 장소, 정원). 정원은 1명 이상이다. */
@Entity
@Table(name = "course_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false, length = 100)
    private String place;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.OPEN;

    static CourseSession create(Course course, LocalDate startDate, String place, int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("정원은 1명 이상이어야 합니다. capacity=" + capacity);
        }
        CourseSession session = new CourseSession();
        session.course = course;
        session.startDate = startDate;
        session.place = place;
        session.capacity = capacity;
        return session;
    }
}
