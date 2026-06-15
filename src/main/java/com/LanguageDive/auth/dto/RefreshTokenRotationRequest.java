package com.LanguageDive.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRotationRequest(
        @NotBlank
        String refreshToken
) {
}
