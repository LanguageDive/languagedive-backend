package com.LanguageDive.content.dto;

import com.LanguageDive.progress.entity.UserLessonProgress;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Progreso de lectura del usuario en una lección específica")
public record LessonProgressResponse(
        @Schema(description = "Índice de la última oración leída (basado en lesson_sentence.sentence_index)", example = "42")
        Integer sentenceIndex,

        @Schema(description = "Indica si el usuario completó la lección", example = "false")
        Boolean completed,

        @Schema(description = "Fecha en que se completó la lección (null si aún no se completó)", example = "2026-07-18T12:00:00Z")
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
