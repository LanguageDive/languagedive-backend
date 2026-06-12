package com.LanguageDive.auth.dto;

import java.time.Instant;

public record AuthUserResponse(
        Long id,
        String username,
        String email,
        Instant createdAt
) {
}
