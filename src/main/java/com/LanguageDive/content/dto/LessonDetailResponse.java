package com.LanguageDive.content.dto;

import com.LanguageDive.content.service.CourseService.VocabularyEntryResponseMap;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "Contenido de una lección con oraciones paginadas y vocabulario del usuario")
public record LessonDetailResponse(
        @Schema(description = "ID de la lección", example = "1")
        long lessonId,

        @Schema(description = "Título del capítulo", example = "The Hare and the Tortoise")
        String lessonTitle,

        @Schema(description = "Oraciones de la página actual")
        List<SentenceResponse> sentences,

        @Schema(description = "Número de página actual (0-based)", example = "0")
        int page,

        @Schema(description = "Tamaño de página solicitado", example = "10")
        int pageSize,

        @Schema(description = "Total de oraciones en la lección", example = "45")
        int totalSentences,

        @Schema(description = "Total de páginas", example = "5")
        int totalPages,

        @Schema(description = "Vocabulario del usuario (mapa de palabra → entry)")
        Map<String, VocabularyEntryResponseMap> vocabulary
) {}
