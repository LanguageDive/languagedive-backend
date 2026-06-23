package com.LanguageDive.vocabulary.dto;

import com.LanguageDive.vocabulary.VocabularyEntry;
import com.LanguageDive.vocabulary.VocabularyStatus;

import java.time.Instant;

public record VocabularyEntryResponse(
    Long id,
    String term,
    String meaning,
    VocabularyStatus status,
    int timesSeen,
    Instant lastSeenAt,
    Instant createdAt,
    Instant updatedAt
) {

    public static VocabularyEntryResponse toDto(VocabularyEntry vocabularyEntry){
        return new VocabularyEntryResponse(
                vocabularyEntry.getId(),
                vocabularyEntry.getTerm(),
                vocabularyEntry.getMeaning(),
                vocabularyEntry.getStatus(),
                vocabularyEntry.getTimesSeen() == null ? 0 : vocabularyEntry.getTimesSeen(),
                vocabularyEntry.getLastSeenAt(),
                vocabularyEntry.getCreatedAt(),
                vocabularyEntry.getUpdatedAt()
        );
    }
}
