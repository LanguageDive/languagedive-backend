package com.LanguageDive.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
        @NotNull
        @Positive
        Long courseId,
        @NotBlank
        @Size(max = 255)
        String title,
        @NotBlank
        String content,
        @NotNull
        @Positive
        Integer lessonOrder,
        @PositiveOrZero
        Integer wordCount
) {
}
