package com.LanguageDive.content.dto;

import com.LanguageDive.progress.UserLessonProgress;

import java.time.Instant;

public record LessonProgressResponse(
        Integer lastReadingPosition,
        Boolean completed,
        Instant completedAt
) {
    public static LessonProgressResponse from(UserLessonProgress progress) {
        return new LessonProgressResponse(
                progress.getLastReadingPosition(),
                progress.getCompleted(),
                progress.getCompletedAt()
        );
    }
}
