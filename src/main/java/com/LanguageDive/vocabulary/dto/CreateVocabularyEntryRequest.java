package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.entity.VocabularyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para agregar una nueva palabra al grimorio del lector")
public record CreateVocabularyEntryRequest(
        @Schema(description = "La palabra en el idioma que se está estudiando", example = "émerveiller")
        @NotBlank @Size(max = 255) String term,

        @Schema(description = "Traducción de la palabra (múltiples separadas por ;)", example = "maravillarse;asombrarse")
        @NotBlank @Size(max = 255) String translation,

        @Schema(description = "Idioma de la traducción", example = "es")
        String translationLang,

        @Schema(description = "Estado inicial de aprendizaje. Por lo general se usa NEW al agregar.", example = "NEW")
        @NotNull VocabularyStatus status
) {
}
