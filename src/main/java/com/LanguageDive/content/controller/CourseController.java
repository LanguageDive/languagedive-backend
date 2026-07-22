package com.LanguageDive.content.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.ImportCourseResponse;
import com.LanguageDive.content.dto.LessonDetailResponse;
import com.LanguageDive.content.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Courses", description = "Gestión del estante de libros del lector")
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    @Operation(summary = "Listar cursos (estante de libros)", description = """
        Devuelve todos los cursos del usuario autenticado. Cada curso incluye:
        - Información básica (título, descripción, portada, idioma)
        - Progreso de lectura (lecciones completadas / totales + porcentaje)

        Es el equivalente al **estante de libros** del frontend.
        """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista de cursos del usuario")})
    public List<CourseListResponse> getCoursesByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return courseService.getAllCoursesByUserId(userId);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Detalle de un curso (tomo)", description = """
        Obtiene la información completa de un curso específico, incluyendo:
        - Datos generales (título, descripción, portada, tipo de fuente, idioma)
        - Lista de todas las lecciones con su progreso individual
        - Progreso global del curso

        Es la vista de un **tomo** abierto con sus capítulos.
        """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle del curso"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado o no pertenece al usuario")
    })
    public CourseDetailResponse getCourseById(
            @PathVariable Long courseId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return courseService.getCourseById(courseId, userPrincipal.getUserId());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Importar un EPUB", description = """
        Sube un archivo EPUB, lo parsea, crea el curso con sus lecciones
        (una por capítulo) y las oraciones de cada lección.

        Devuelve el ID del curso creado para redirigir al frontend.
        """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Curso creado desde EPUB"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o corrupto"),
        @ApiResponse(responseCode = "413", description = "Archivo excede el tamaño máximo (10MB)")
    })
    public ImportCourseResponse importEpub(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("file")
                    @Parameter(
                            description = "Archivo EPUB a importar",
                            content = @Content(mediaType = "application/octet-stream"))
                    MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        return courseService.importEpub(userPrincipal.getUserId(), file, description);
    }

    @GetMapping("/{courseId}/lessons/{lessonId}")
    @Operation(summary = "Obtener lección con oraciones paginadas", description = """
        Devuelve las oraciones de una lección paginadas.

        El vocabulario del usuario se obtiene por separado mediante
        GET /api/vocabulary para colorear las palabras conocidas.

        Parámetros opcionales: page (default 0), pageSize (default 10).
        """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contenido de la lección"),
        @ApiResponse(responseCode = "404", description = "Lección no encontrada")
    })
    public LessonDetailResponse getLessonPage(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return courseService.getLessonPage(lessonId, userPrincipal.getUserId(), page, pageSize);
    }
}
