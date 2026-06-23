package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.VocabularyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateVocabularyEntryRequest(
        @NotBlank
        @Size(max = 255)
        String term,
        @NotBlank
        @Size(max = 255)
        String meaning,
        @NotNull
        VocabularyStatus status
) {
}
