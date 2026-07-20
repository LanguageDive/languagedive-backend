package com.LanguageDive.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear una nueva lección/capítulo dentro de un curso")
public record CreateLessonRequest(
        @Schema(description = "ID del curso al que pertenece esta lección", example = "1")
        @NotNull @Positive Long courseId,

        @Schema(description = "Título del capítulo/lección", example = "Capítulo 1: El encuentro con el principito")
        @NotBlank @Size(max = 255) String title,

        @Schema(description = "Contenido textual completo de la lección", example = "Cuando yo tenía seis años, vi una vez una lámina magnífica...")
        @NotBlank String content,

        @Schema(description = "Orden dentro del curso (1, 2, 3...). Define la secuencia de lectura.", example = "1")
        @NotNull @Positive Integer lessonOrder,

        @Schema(description = "Cantidad de palabras del contenido (opcional, se calcula automáticamente si no se envía)", example = "1200")
        @PositiveOrZero Integer wordCount
) {
}
