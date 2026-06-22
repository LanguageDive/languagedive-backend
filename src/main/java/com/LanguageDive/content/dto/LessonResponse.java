package com.LanguageDive.content.dto;

import com.LanguageDive.content.Lesson;

import java.time.Instant;

public record LessonResponse(
        Long id,
        Long courseId,
        String title,
        String content,
        Integer lessonOrder,
        Integer wordCount,
        Instant createdAt,
        Instant updatedAt,
        LessonProgressResponse progress
) {
    public static LessonResponse from(Lesson lesson, LessonProgressResponse progress) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getCourse().getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getLessonOrder(),
                lesson.getWordCount(),
                lesson.getCreatedAt(),
                lesson.getUpdatedAt(),
                progress
        );
    }
}
