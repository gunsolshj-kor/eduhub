package com.example.eduhub.common.error;

import com.example.eduhub.course.CourseClosedException;
import com.example.eduhub.course.CourseNotFoundException;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 예외를 ErrorResponse(code, message)로 바꾼다. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFound(CourseNotFoundException e) {
        return toResponse(ErrorCode.COURSE_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CourseClosedException.class)
    public ResponseEntity<ErrorResponse> handleCourseClosed(CourseClosedException e) {
        return toResponse(ErrorCode.COURSE_CLOSED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return toResponse(ErrorCode.INVALID_INPUT, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return toResponse(ErrorCode.INVALID_INPUT, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, message));
    }
}
