package com.LanguageDive.content.dto;

import com.LanguageDive.content.entity.SourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear un nuevo curso/libro en el estante del lector")
public record CreateCourseRequest(
        @Schema(description = "Título del curso o libro", example = "El Principito")
        @NotBlank @Size(max = 255) String title,

        @Schema(description = "Descripción breve del contenido", example = "Un piloto perdido en el desierto conoce a un pequeño príncipe.")
        @Size(max = 255) String description,

        @Schema(description = "URL de la imagen de portada (opcional)", example = "https://ejemplo.com/portada.jpg")
        @Size(max = 255) String coverUrl,

        @Schema(description = "Formato del contenido fuente: EPUB (libro digital) o TXT (texto plano)", example = "EPUB")
        @NotNull SourceType sourceType
) {
}
