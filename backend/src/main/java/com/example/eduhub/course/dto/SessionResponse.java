package com.example.eduhub.course.dto;

import com.example.eduhub.course.CourseSession;
import com.example.eduhub.course.SessionStatus;
import java.time.LocalDate;

/** 회차 (FR-02). 수강 신청(FR-04) 전이므로 남은 자리는 정원과 같다. */
public record SessionResponse(
        Long id, LocalDate startDate, String place, int capacity, int remainingSeats, SessionStatus status) {

    public static SessionResponse from(CourseSession session) {
        return new SessionResponse(
                session.getId(),
                session.getStartDate(),
                session.getPlace(),
                session.getCapacity(),
                session.getCapacity(),
                session.getStatus());
    }
}
