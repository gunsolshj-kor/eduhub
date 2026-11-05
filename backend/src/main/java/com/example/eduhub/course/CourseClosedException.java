package com.example.eduhub.course;

public class CourseClosedException extends RuntimeException {

    public CourseClosedException(Long id) {
        super("종료된 과정은 변경할 수 없습니다. id=" + id);
    }
}
