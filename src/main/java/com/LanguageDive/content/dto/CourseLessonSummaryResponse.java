package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Lesson;

public record CourseLessonSummaryResponse(
        Long id,
        String title,
        Integer lessonOrder,
        Integer wordCount,
        LessonProgressResponse progress
) {
    public static CourseLessonSummaryResponse from(Lesson lesson, LessonProgressResponse progress) {
        return new CourseLessonSummaryResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getLessonOrder(),
                lesson.getWordCount(),
                progress
        );
    }
}
