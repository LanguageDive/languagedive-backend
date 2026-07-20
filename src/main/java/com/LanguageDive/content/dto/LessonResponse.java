package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Contenido completo de una lección para la vista de lectura")
public record LessonResponse(
        @Schema(description = "ID único de la lección", example = "1")
        Long id,

        @Schema(description = "ID del curso al que pertenece", example = "1")
        Long courseId,

        @Schema(description = "Título del capítulo", example = "Capítulo 1: El encuentro con el principito")
        String title,

        @Schema(description = "Contenido textual completo de la lección")
        String content,

        @Schema(description = "Orden dentro del curso", example = "1")
        Integer lessonOrder,

        @Schema(description = "Cantidad de palabras del contenido", example = "1200")
        Integer wordCount,

        @Schema(description = "Fecha de creación", example = "2026-07-18T12:00:00Z")
        Instant createdAt,

        @Schema(description = "Fecha de última modificación", example = "2026-07-18T12:00:00Z")
        Instant updatedAt,

        @Schema(description = "Progreso de lectura del usuario en esta lección")
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
