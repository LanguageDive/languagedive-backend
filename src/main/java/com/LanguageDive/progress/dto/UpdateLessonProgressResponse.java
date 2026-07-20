package com.LanguageDive.progress.dto;

import com.LanguageDive.progress.entity.UserLessonProgress;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Progreso de lectura actualizado")
public record UpdateLessonProgressResponse(
        @Schema(description = "ID de la lección", example = "1")
        Long lessonId,

        @Schema(description = "Última posición guardada (0-100)", example = "75")
        int lastReadingPosition,

        @Schema(description = "Si la lección está completada", example = "false")
        Boolean completed,

        @Schema(description = "Cuándo se completó (null si aún no)", example = "2026-07-18T12:00:00Z")
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
