package com.LanguageDive.progress.dto;

import com.LanguageDive.progress.UserLessonProgress;

import java.time.Instant;

public record UpdateLessonProgressResponse(
        Long lessonId,
        int lastReadingPosition,
        Boolean completed,
        Instant completedAt
) {
    static public UpdateLessonProgressResponse toDto(UserLessonProgress entity){
        return new UpdateLessonProgressResponse(
                entity.getLesson().getId(),
                entity.getLastReadingPosition(),
                entity.getCompleted(),
                entity.getCompletedAt()
        );
    }
}
