package com.LanguageDive.progress.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.progress.service.UserLessonProgressService;
import com.LanguageDive.progress.dto.UpdateLessonProgressRequest;
import com.LanguageDive.progress.dto.UpdateLessonProgressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/progress")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Progress", description = "Seguimiento del progreso de lectura")
public class ProgressController {

    private final UserLessonProgressService userLessonProgressService;

    @PutMapping("/lessons/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Guardar progreso de lectura",
            description = """
                    Actualiza el progreso del usuario en una lección específica.

                    El `lastReadingPosition` indica hasta dónde leyó el usuario (0-100% del scroll).
                    El flag `completed` indica si el usuario terminó la lección.

                    El frontend llama a este endpoint automáticamente mientras el usuario lee:
                    - Cada 2 segundos después de dejar de scrollear
                    - Al salir de la página
                    - Cuando el usuario llega al final de la lección (completed = true)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progreso guardado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lección no encontrada o no pertenece al usuario")
    })
    public UpdateLessonProgressResponse updateLessonProgressByUserIdAndLessonId(
            @PathVariable("lessonId") Long lessonId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateLessonProgressRequest updateLessonProgressRequest) {
        return userLessonProgressService.updateProgress(userPrincipal.getUserId(), lessonId, updateLessonProgressRequest);
    }
}
