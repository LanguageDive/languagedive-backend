package com.LanguageDive.vocabulary.repository;

import com.LanguageDive.vocabulary.entity.VocabularyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyEntryRepository extends JpaRepository<VocabularyEntry, Long> {

    List<VocabularyEntry> findAllByUserId(Long userId);

    Optional<VocabularyEntry> findByIdAndUserId(Long id, Long userId);

    Optional<VocabularyEntry> findByTermAndUserId(String term, Long userId);
}
