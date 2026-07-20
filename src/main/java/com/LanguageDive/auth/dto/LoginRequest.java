package com.LanguageDive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Credenciales para iniciar sesión en el grimorio")
public record LoginRequest(
        @Schema(description = "Nombre de usuario o email registrado", example = "lector_aventurero")
        @NotBlank @Size(max = 255) String identifier,

        @Schema(description = "Contraseña del usuario", example = "miContraseñaSegura123")
        @NotBlank @Size(max = 255) String password
) {
}
