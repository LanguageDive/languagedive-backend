package com.LanguageDive.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Una oración dentro de una lección")
public record SentenceResponse(
        @Schema(description = "ID de la oración", example = "1")
        Long id,

        @Schema(description = "Índice secuencial dentro de la lección (0-based)", example = "0")
        int index,

        @Schema(description = "Texto de la oración", example = "Once upon a time there was a little prince.")
        String text
) {}
