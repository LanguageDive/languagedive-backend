package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.entity.VocabularyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar el significado o estado de aprendizaje de una palabra")
public record UpdateVocabularyEntryRequest(
        @Schema(description = "Nueva traducción (opcional, solo si se quiere corregir)", example = "sorprenderse;admirarse")
        @Size(max = 255) String translation,

        @Schema(description = "Nuevo estado de aprendizaje", example = "LEVEL_2")
        VocabularyStatus status
) {
}
