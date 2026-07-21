package com.LanguageDive.content.dto;

import com.LanguageDive.progress.entity.UserCourseProgress;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Progreso de lectura del usuario en un curso")
public record CourseProgressResponse(
        @Schema(description = "Cantidad de lecciones completadas", example = "3")
        Integer completedLessons,

        @Schema(description = "Cantidad total de lecciones del curso", example = "10")
        Integer totalLessons,

        @Schema(description = "Porcentaje de progreso (0-100)", example = "30")
        Integer progressPercentage,

        @Schema(description = "Total de oraciones en el curso", example = "450")
        Integer totalSentences,

        @Schema(description = "Oraciones leídas/completadas", example = "120")
        Integer completedSentences,

        @Schema(description = "Palabras conocidas (nivel > LEVEL_1)", example = "25")
        Integer vocabularyKnown,

        @Schema(description = "Palabras en aprendizaje", example = "8")
        Integer vocabularyLearning
) {
    public static CourseProgressResponse from(UserCourseProgress progress) {
        return new CourseProgressResponse(
                progress.getCompletedLessons(),
                progress.getTotalLessons(),
                progress.getProgressPercentage(),
                progress.getTotalSentences(),
                progress.getCompletedSentences(),
                progress.getVocabularyKnown(),
                progress.getVocabularyLearning()
        );
    }
}
