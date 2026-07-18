package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.entity.VocabularyStatus;
import jakarta.validation.constraints.Size;

public record UpdateVocabularyEntryRequest(
        @Size(max = 255)
        String meaning,
        VocabularyStatus status
) {
}
