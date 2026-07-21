package com.LanguageDive.vocabulary.service;

import com.LanguageDive.common.exception.InvalidRequestException;
import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.service.UserService;
import com.LanguageDive.vocabulary.entity.VocabularyEntry;
import com.LanguageDive.vocabulary.entity.VocabularyStatus;
import com.LanguageDive.vocabulary.repository.VocabularyEntryRepository;
import com.LanguageDive.vocabulary.dto.CreateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.UpdateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.VocabularyEntriesResponse;
import com.LanguageDive.vocabulary.dto.VocabularyEntryResponse;
import com.LanguageDive.vocabulary.dto.VocabularyUpsertResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class VocabularyEntryService {

    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public VocabularyEntriesResponse getVocabularyByUserId(Long userId) {
        List<VocabularyEntry> vocabularyEntries = vocabularyEntryRepository.findAllByUserId(userId);

        return new VocabularyEntriesResponse(
                vocabularyEntries.stream().map(VocabularyEntryResponse::toDto).toList()
        );
    }

    @Transactional
    public VocabularyUpsertResponse createOrUpdateVocabulary(Long userId, CreateVocabularyEntryRequest request) {
        String normalizedTerm = normalizeTerm(request.term());

        return vocabularyEntryRepository.findByTermAndUserId(normalizedTerm, userId)
                .map(entry -> updateExistingEntry(entry, request))
                .orElseGet(() -> createEntry(userId, normalizedTerm, request));
    }

    @Transactional
    public VocabularyEntryResponse updateVocabulary(Long userId, Long vocabularyId, UpdateVocabularyEntryRequest request) {
        VocabularyEntry vocabularyEntry = vocabularyEntryRepository.findByIdAndUserId(vocabularyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vocabulary entry", "id", vocabularyId));

        boolean hasTranslation = request.translation() != null;
        boolean hasStatus = request.status() != null;

        if (!hasTranslation && !hasStatus) {
            throw new InvalidRequestException("At least one updatable field must be provided");
        }

        if (hasTranslation) {
            String normalizedTranslation = request.translation().trim();
            if (normalizedTranslation.isEmpty()) {
                throw new InvalidRequestException("Translation must not be blank");
            }
            vocabularyEntry.setTranslation(normalizedTranslation);
        }

        if (hasStatus) {
            vocabularyEntry.setStatus(request.status());
        }

        VocabularyEntry savedEntry = vocabularyEntryRepository.save(vocabularyEntry);
        return VocabularyEntryResponse.toDto(savedEntry);
    }

    private VocabularyUpsertResponse updateExistingEntry(VocabularyEntry vocabularyEntry, CreateVocabularyEntryRequest request) {
        vocabularyEntry.setTranslation(request.translation().trim());
        vocabularyEntry.setTranslationLang(request.translationLang());
        vocabularyEntry.setStatus(request.status());

        VocabularyEntry savedEntry = vocabularyEntryRepository.save(vocabularyEntry);
        return new VocabularyUpsertResponse(VocabularyEntryResponse.toDto(savedEntry), false);
    }

    private VocabularyUpsertResponse createEntry(Long userId, String normalizedTerm, CreateVocabularyEntryRequest request) {
        User user = userService.findById(userId);

        VocabularyEntry vocabularyEntry = new VocabularyEntry();
        vocabularyEntry.setTerm(normalizedTerm);
        vocabularyEntry.setTranslation(request.translation().trim());
        vocabularyEntry.setTranslationLang(request.translationLang());
        vocabularyEntry.setStatus(request.status());
        vocabularyEntry.setTimesSeen(0);
        vocabularyEntry.setLastSeenAt(null);
        vocabularyEntry.setUser(user);

        VocabularyEntry savedEntry = vocabularyEntryRepository.save(vocabularyEntry);
        return new VocabularyUpsertResponse(VocabularyEntryResponse.toDto(savedEntry), true);
    }

    private String normalizeTerm(String term) {
        return term.trim().toLowerCase(Locale.ROOT);
    }
}
