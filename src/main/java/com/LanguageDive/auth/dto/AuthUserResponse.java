package com.LanguageDive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Información básica del usuario/lector")
public record AuthUserResponse(
        @Schema(description = "ID único del usuario", example = "1")
        Long id,

        @Schema(description = "Nombre de usuario", example = "lector_aventurero")
        String username,

        @Schema(description = "Email del usuario", example = "lector@ejemplo.com")
        String email,

        @Schema(description = "Fecha de creación de la cuenta", example = "2026-07-18T12:00:00Z")
        Instant createdAt
) {
}
