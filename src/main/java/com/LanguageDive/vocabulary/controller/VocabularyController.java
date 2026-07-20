package com.LanguageDive.vocabulary.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.vocabulary.dto.CreateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.UpdateVocabularyEntryRequest;
import com.LanguageDive.vocabulary.dto.VocabularyEntriesResponse;
import com.LanguageDive.vocabulary.dto.VocabularyEntryResponse;
import com.LanguageDive.vocabulary.dto.VocabularyUpsertResponse;
import com.LanguageDive.vocabulary.service.VocabularyEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Vocabulary", description = "Grimorio personal: palabras guardadas durante la lectura")
public class VocabularyController {
    private final VocabularyEntryService vocabularyEntryService;

    @GetMapping()
    @Operation(
            summary = "Listar grimorio (vocabulario guardado)",
            description = """
                    Devuelve todas las palabras que el usuario ha guardado durante su lectura,
                    con su estado de aprendizaje actual.

                    Cada entrada incluye:
                    - La palabra (term) y su significado
                    - Estado de aprendizaje (NEW → LEVEL_2-5 → LEARNED, o IGNORED)
                    - Estadísticas (veces vista, última vez)

                    Es el **grimorio personal** del lector.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista del vocabulario guardado")
    })
    public VocabularyEntriesResponse getVocabularyByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return vocabularyEntryService.getVocabularyByUserId(userPrincipal.getUserId());
    }

    @PostMapping
    @Operation(
            summary = "Agregar o actualizar palabra en el grimorio",
            description = """
                    Guarda una nueva palabra en el grimorio del usuario. Si ya existe una entrada
                    con el mismo término para este usuario, la actualiza (upsert).

                    Útil cuando el lector toca una palabra en el texto y quiere guardarla
                    con su significado para estudiarla después.

                    Los estados disponibles son:
                    - **NEW**: recién agregada
                    - **LEVEL_2** a **LEVEL_5**: niveles de práctica intermedios
                    - **LEARNED**: el usuario ya la aprendió
                    - **IGNORED**: el usuario no quiere estudiar esta palabra
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Palabra nueva agregada al grimorio"),
            @ApiResponse(responseCode = "200", description = "Palabra existente actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (term vacío, status inválido)")
    })
    public ResponseEntity<VocabularyEntryResponse> createOrUpdateVocabulary(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateVocabularyEntryRequest request) {
        VocabularyUpsertResponse response =
                vocabularyEntryService.createOrUpdateVocabulary(userPrincipal.getUserId(), request);
        if (response.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response.entry());
        }

        return ResponseEntity.ok(response.entry());
    }

    @PatchMapping("/{vocabularyId}")
    @Operation(
            summary = "Actualizar estado/significado de una palabra",
            description = """
                    Actualiza el significado y/o el estado de aprendizaje de una palabra existente
                    en el grimorio del usuario.

                    Ejemplos de uso:
                    - El usuario aprendió una palabra → cambiar status a LEARNED
                    - El usuario corrige el significado
                    - El usuario ignora una palabra → cambiar status a IGNORED
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Palabra actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada o no pertenece al usuario")
    })
    public VocabularyEntryResponse updateVocabulary(
            @PathVariable Long vocabularyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateVocabularyEntryRequest request) {
        return vocabularyEntryService.updateVocabulary(userPrincipal.getUserId(), vocabularyId, request);
    }
}
