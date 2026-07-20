package com.LanguageDive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para renovar tokens o cerrar sesión usando el refresh token")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token obtenido previamente en login/register/refresh", example = "eyJhbGciOiJIUzI1NiJ9...")
        @NotBlank
        String refreshToken
) {
}
