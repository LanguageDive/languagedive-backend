package com.LanguageDive.vocabulary.dto;

public record VocabularyUpsertResponse(
        VocabularyEntryResponse entry,
        boolean created
) {
}
