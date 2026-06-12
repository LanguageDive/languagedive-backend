package com.LanguageDive.auth.dto;

public record AuthResponse(
        AuthUserResponse user,
        String accessToken,
        String refreshToken
) {
}
