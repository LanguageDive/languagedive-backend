package com.LanguageDive.content.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.dto.CreateLessonRequest;
import com.LanguageDive.content.dto.LessonResponse;
import com.LanguageDive.content.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Lessons", description = "Contenido de las lecciones (capítulos)")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lessonId}")
    @Operation(
            summary = "Obtener contenido de una lección",
            description = """
                    Devuelve el contenido completo de una lección para leer, incluyendo:
                    - Título del capítulo
                    - Contenido textual
                    - Progreso de lectura del usuario (posición, completado)

                    El `lessonId` debe corresponder a una lección dentro de un curso del usuario autenticado.
                    Es el endpoint principal para la **vista de lectura** del frontend.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido de la lección"),
            @ApiResponse(responseCode = "404", description = "Lección no encontrada o no pertenece al usuario")
    })
    public LessonResponse getLessonById(
            @PathVariable Long lessonId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return lessonService.getLessonById(lessonId, userPrincipal.getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear una nueva lección",
            description = """
                    Agrega una lección/capítulo a un curso existente.

                    El `lessonOrder` define la posición del capítulo dentro del curso (1, 2, 3...).
                    Si no se proporciona `wordCount`, el sistema lo calcula automáticamente.

                    El `courseId` debe corresponder a un curso del usuario autenticado.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lección creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (título vacío, courseId inválido, orden inválido)"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado o no pertenece al usuario")
    })
    public LessonResponse createLesson(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CreateLessonRequest request) {
        return lessonService.createLesson(userPrincipal.getUserId(), request);
    }
}
