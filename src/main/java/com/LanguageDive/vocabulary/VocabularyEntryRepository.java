package com.LanguageDive.vocabulary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyEntryRepository extends JpaRepository<VocabularyEntry, Long> {
}
