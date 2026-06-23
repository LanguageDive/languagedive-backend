package com.LanguageDive.vocabulary.dto;

import java.util.List;

public record VocabularyEntriesResponse(
        List<VocabularyEntryResponse> entries
) {
}
