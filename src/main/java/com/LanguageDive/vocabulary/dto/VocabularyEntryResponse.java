package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.entity.VocabularyEntry;
import com.LanguageDive.vocabulary.entity.VocabularyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Una palabra guardada en el grimorio del lector con su estado de aprendizaje")
public record VocabularyEntryResponse(
        @Schema(description = "ID único de la entrada de vocabulario", example = "1")
        Long id,

        @Schema(description = "La palabra en el idioma estudiado", example = "émerveiller")
        String term,

        @Schema(description = "Significado o traducción", example = "maravillarse, asombrarse")
        String translation,

        @Schema(description = "Idioma de la traducción", example = "es")
        String translationLang,

        @Schema(description = "Estado de aprendizaje: NEW → LEVEL_2-5 → LEARNED, o IGNORED", example = "NEW")
        VocabularyStatus status,

        @Schema(description = "Cantidad de veces que el usuario vio esta palabra", example = "0")
        int timesSeen,

        @Schema(description = "Última vez que el usuario vio esta palabra", example = "2026-07-18T12:00:00Z")
        Instant lastSeenAt,

        @Schema(description = "Fecha en que se agregó la palabra", example = "2026-07-18T12:00:00Z")
        Instant createdAt,

        @Schema(description = "Última modificación", example = "2026-07-18T12:00:00Z")
        Instant updatedAt
) {
    public static VocabularyEntryResponse toDto(VocabularyEntry vocabularyEntry){
        return new VocabularyEntryResponse(
                vocabularyEntry.getId(),
                vocabularyEntry.getTerm(),
                vocabularyEntry.getTranslation(),
                vocabularyEntry.getTranslationLang(),
                vocabularyEntry.getStatus(),
                vocabularyEntry.getTimesSeen() == null ? 0 : vocabularyEntry.getTimesSeen(),
                vocabularyEntry.getLastSeenAt(),
                vocabularyEntry.getCreatedAt(),
                vocabularyEntry.getUpdatedAt()
        );
    }
}
