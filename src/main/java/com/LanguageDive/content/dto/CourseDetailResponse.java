package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Detalle completo de un curso, incluyendo sus lecciones y progreso")
public record CourseDetailResponse(
        @Schema(description = "ID único del curso", example = "1")
        Long id,

        @Schema(description = "Título del libro/curso", example = "El Principito")
        String title,

        @Schema(description = "Descripción breve", example = "Un piloto perdido en el desierto conoce a un pequeño príncipe.")
        String description,

        @Schema(description = "URL de la imagen de portada", example = "https://ejemplo.com/portada.jpg")
        String coverUrl,

        @Schema(description = "Formato del contenido: EPUB o TXT", example = "EPUB")
        String sourceType,

        @Schema(description = "Fecha de creación del curso", example = "2026-07-18T12:00:00Z")
        Instant createdAt,

        @Schema(description = "Progreso global de lectura en este curso")
        CourseProgressResponse progress,

        @Schema(description = "Lista de lecciones/capítulos del curso con su progreso individual")
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
