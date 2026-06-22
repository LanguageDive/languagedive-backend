package com.LanguageDive.content.dto;

import com.LanguageDive.content.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank
        @Size(max = 255)
        String title,
        @Size(max = 255)
        String description,
        @Size(max = 255)
        String coverUrl,
        @NotNull
        SourceType sourceType
) {
}
