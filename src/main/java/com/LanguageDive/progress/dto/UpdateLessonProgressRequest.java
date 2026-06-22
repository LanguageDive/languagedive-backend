package com.LanguageDive.progress.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateLessonProgressRequest(
        @NotNull
        @PositiveOrZero
        int lastReadingPosition,
        @NotNull
        Boolean completed
) {
}
