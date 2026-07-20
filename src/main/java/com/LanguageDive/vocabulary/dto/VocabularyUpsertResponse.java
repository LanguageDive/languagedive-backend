package com.LanguageDive.vocabulary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de la operación upsert (crear o actualizar) de una palabra")
public record VocabularyUpsertResponse(
        @Schema(description = "La entrada de vocabulario resultante")
        VocabularyEntryResponse entry,

        @Schema(description = "true si se creó una nueva entrada, false si se actualizó una existente", example = "true")
        boolean created
) {
}
