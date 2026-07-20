package com.LanguageDive.vocabulary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Lista de palabras del grimorio del lector")
public record VocabularyEntriesResponse(
        @Schema(description = "Entradas de vocabulario del usuario")
        List<VocabularyEntryResponse> entries
) {
}
