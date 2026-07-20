package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de una lección para mostrar en la tabla de contenidos del curso")
public record CourseLessonSummaryResponse(
        @Schema(description = "ID único de la lección", example = "1")
        Long id,

        @Schema(description = "Título del capítulo", example = "Capítulo 1: El encuentro")
        String title,

        @Schema(description = "Orden dentro del curso", example = "1")
        Integer lessonOrder,

        @Schema(description = "Cantidad de palabras del capítulo", example = "1200")
        Integer wordCount,

        @Schema(description = "Progreso de lectura del usuario en esta lección")
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
