package com.example.eduhub.course;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super("과정을 찾을 수 없습니다. id=" + id);
    }
}
