package com.LanguageDive.auth.dto;

import com.LanguageDive.auth.entity.RefreshToken;

public record RefreshTokenIssuance(String rawToken, RefreshToken refreshToken) {
}
