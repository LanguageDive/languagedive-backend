package com.LanguageDive.content.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.CreateCourseRequest;
import com.LanguageDive.content.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/courses")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Courses", description = "Gestión del estante de libros del lector")
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    @Operation(
            summary = "Listar cursos (estante de libros)",
            description = """
                    Devuelve todos los cursos del usuario autenticado. Cada curso incluye:
                    - Información básica (título, descripción, portada)
                    - Progreso de lectura (lecciones completadas / totales + porcentaje)

                    Es el equivalente al **estante de libros** del frontend.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cursos del usuario")
    })
    public List<CourseListResponse> getCoursesByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return courseService.getAllCoursesByUserId(userId);
    }

    @GetMapping("/{courseId}")
    @Operation(
            summary = "Detalle de un curso (tomo)",
            description = """
                    Obtiene la información completa de un curso específico, incluyendo:
                    - Datos generales (título, descripción, portada, tipo de fuente)
                    - Lista de todas las lecciones con su progreso individual
                    - Progreso global del curso

                    Es la vista de un **tomo** abierto con sus capítulos.
                    El `courseId` debe pertenecer al usuario autenticado.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle del curso"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado o no pertenece al usuario")
    })
    public CourseDetailResponse getCourseById(
            @PathVariable Long courseId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return courseService.getCourseById(courseId, userPrincipal.getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo curso",
            description = """
                    Agrega un nuevo curso/libro al estante del usuario.

                    El `sourceType` indica el formato del contenido fuente:
                    - **EPUB**: para libros digitales en formato ePub
                    - **TXT**: para texto plano

                    Después de crear el curso, podés ir agregando lecciones via `POST /api/lessons`.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (título vacío, sourceType inválido)")
    })
    public CourseDetailResponse createCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CreateCourseRequest request) {
        return courseService.createCourse(userPrincipal.getUserId(), request);
    }
}
