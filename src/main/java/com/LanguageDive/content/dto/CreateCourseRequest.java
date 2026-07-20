package com.LanguageDive.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = """
        Datos para crear un nuevo curso. El título puede extraerse automáticamente
        de los metadatos del archivo EPUB si no se envía.
        """)
public record CreateCourseRequest(
        @Schema(description = "Título del curso/libro (opcional — se extrae de metadatos EPUB si no se envía)", example = "El Principito")
        @Size(max = 255) String title,

        @Schema(description = "Descripción breve (opcional — se extrae de metadatos EPUB si no se envía)", example = "Un piloto perdido en el desierto conoce a un pequeño príncipe.")
        @Size(max = 255) String description
) {
}
