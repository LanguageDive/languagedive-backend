package com.LanguageDive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con datos del usuario y tokens")
public record AuthResponse(
        @Schema(description = "Datos del usuario autenticado")
        AuthUserResponse user,

        @Schema(description = "Token JWT de corta duración (15 min). Enviar en header Authorization: Bearer <token>", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(description = "Token de larga duración (7 días) para renovar el access token sin re-loguearse", example = "eyJhbGciOiJIUzI1NiJ9...")
        String refreshToken
) {
}
