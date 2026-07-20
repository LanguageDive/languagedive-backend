package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Resumen de un curso para mostrar en el estante de libros")
public record CourseListResponse(
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

        @Schema(description = "Progreso de lectura del usuario en este curso")
        CourseProgressResponse progress
) {
    public static CourseListResponse from(Course course, CourseProgressResponse progress) {
        return new CourseListResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCoverUrl(),
                course.getSourceType().name(),
                course.getCreatedAt(),
                progress
        );
    }
}
