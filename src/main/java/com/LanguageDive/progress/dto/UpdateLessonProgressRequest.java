package com.LanguageDive.progress.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Datos para actualizar el progreso de lectura en una lección")
public record UpdateLessonProgressRequest(
        @Schema(description = "Posición actual de lectura como porcentaje (0 = inicio, 100 = final)", example = "75")
        @NotNull @PositiveOrZero int lastReadingPosition,

        @Schema(description = "Indica si el usuario terminó de leer esta lección", example = "false")
        @NotNull Boolean completed
) {
}
