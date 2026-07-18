package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Course;

import java.time.Instant;

public record CourseListResponse(
        Long id,
        String title,
        String description,
        String coverUrl,
        String sourceType,
        Instant createdAt,
        CourseProgressResponse progress
) {
    public static CourseListResponse from(Course course, CourseProgressResponse progress) {
        return new CourseListResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCoverUrl(),
                course.getSourceType().name(),
                course.getCreatedAt(),
                progress
        );
    }
}
