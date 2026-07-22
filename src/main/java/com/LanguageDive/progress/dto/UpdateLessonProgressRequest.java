package com.LanguageDive.progress.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Datos para actualizar el progreso de lectura en una lección")
public record UpdateLessonProgressRequest(
        @Schema(description = "Índice de la oración actual donde se quedó el usuario (basado en lesson_sentence.sentence_index)", example = "42")
        @NotNull @PositiveOrZero int sentenceIndex,

        @Schema(description = "Indica si el usuario terminó de leer esta lección", example = "false")
        @NotNull Boolean completed
) {
}
