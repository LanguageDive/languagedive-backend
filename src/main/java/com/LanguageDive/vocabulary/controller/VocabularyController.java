package com.LanguageDive.vocabulary.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.vocabulary.service.VocabularyEntryService;
import com.LanguageDive.vocabulary.dto.CreateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.UpdateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.VocabularyEntriesResponse;
import com.LanguageDive.vocabulary.dto.VocabularyEntryResponse;
import com.LanguageDive.vocabulary.dto.VocabularyUpsertResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vocabulary")
public class VocabularyController {
    private final VocabularyEntryService vocabularyEntryService;

    @GetMapping()
    public VocabularyEntriesResponse getVocabularyByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return vocabularyEntryService.getVocabularyByUserId(userPrincipal.getUserId());
    }

    @PostMapping
    public ResponseEntity<VocabularyEntryResponse> createOrUpdateVocabulary(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateVocabularyEntryRequest request
    ) {
        VocabularyUpsertResponse response = vocabularyEntryService.createOrUpdateVocabulary(userPrincipal.getUserId(), request);
        if (response.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response.entry());
        }

        return ResponseEntity.ok(response.entry());
    }

    @PatchMapping("/{vocabularyId}")
    public VocabularyEntryResponse updateVocabulary(
            @PathVariable Long vocabularyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateVocabularyEntryRequest request
    ) {
        return vocabularyEntryService.updateVocabulary(
                userPrincipal.getUserId(),
                vocabularyId,
                request
        );
    }

}
