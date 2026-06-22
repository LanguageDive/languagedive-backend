package com.LanguageDive.content.dto;

import com.LanguageDive.content.Course;

import java.time.Instant;
import java.util.List;

public record CourseDetailResponse(
        Long id,
        String title,
        String description,
        String coverUrl,
        String sourceType,
        Instant createdAt,
        CourseProgressResponse progress,
        List<CourseLessonSummaryResponse> lessons
) {
    public static CourseDetailResponse from(
            Course course,
            CourseProgressResponse progress,
            List<CourseLessonSummaryResponse> lessons
    ) {
        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCoverUrl(),
                course.getSourceType().name(),
                course.getCreatedAt(),
                progress,
                lessons
        );
    }
}
