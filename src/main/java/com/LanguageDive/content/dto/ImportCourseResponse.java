package com.LanguageDive.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta tras importar un EPUB y crear el curso automáticamente")
public record ImportCourseResponse(
        @Schema(description = "ID del curso creado", example = "1")
        Long id,

        @Schema(description = "Título extraído del EPUB", example = "Children's Stories")
        String title,

        @Schema(description = "Descripción opcional provista por el usuario", example = "Libro para practicar inglés")
        String description,

        @Schema(description = "Idioma detectado del EPUB", example = "en")
        String language,

        @Schema(description = "Cantidad de lecciones/capítulos creados", example = "12")
        int lessonCount,

        @Schema(description = "Total de oraciones en el curso", example = "450")
        int totalSentences
) {}
