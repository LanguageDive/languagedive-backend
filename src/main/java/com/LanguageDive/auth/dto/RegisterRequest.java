package com.LanguageDive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear una nueva cuenta de lector en LanguageDive")
public record RegisterRequest(
        @Schema(description = "Nombre único de usuario (visible para otros lectores)", example = "lector_aventurero")
        @NotBlank @Size(max = 255) String username,

        @Schema(description = "Correo electrónico válido (se usa también para iniciar sesión)", example = "lector@ejemplo.com")
        @NotBlank @Email @Size(max = 255) String email,

        @Schema(description = "Contraseña (mínimo 8 caracteres)", example = "miContraseñaSegura123", minLength = 8)
        @NotBlank @Size(min = 8, max = 255) String password
) {
}
