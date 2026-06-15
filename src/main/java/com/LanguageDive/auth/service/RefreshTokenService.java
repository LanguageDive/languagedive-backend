package com.LanguageDive.auth.service;

import com.LanguageDive.auth.dto.RefreshTokenIssuance;
import com.LanguageDive.auth.entity.RefreshToken;
import com.LanguageDive.auth.repository.RefreshTokenRepository;
import com.LanguageDive.common.exception.InvalidRefreshTokenException;
import com.LanguageDive.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshTokenIssuance createRefreshToken(User user){
        String rawToken = generateRefreshToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(REFRESH_TOKEN_TTL));
        refreshToken.setTokenHash(hashRefreshToken(rawToken));

        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new RefreshTokenIssuance(rawToken, savedRefreshToken);
    }

    @Transactional
    public RefreshToken validateRefreshToken(String rawRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(hashRefreshToken(rawRefreshToken))
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

        if (refreshToken.getRevokedAt() != null) {
            revokeAllActiveRefreshTokensForUser(refreshToken.getUser());
            throw new InvalidRefreshTokenException("Refresh token revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token expired");
        }

        return refreshToken;
    }

    @Transactional
    public RefreshToken revokeRefreshToken(RefreshToken refreshToken) {
        if (refreshToken.getRevokedAt() == null) {
            refreshToken.setRevokedAt(Instant.now());
            return refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    @Transactional
    public void revokeRefreshTokenByRawToken(String rawRefreshToken) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository
                .findByTokenHash(hashRefreshToken(rawRefreshToken));

        refreshTokenOptional.ifPresent(this::revokeRefreshToken);
    }

    @Transactional
    public void revokeAllActiveRefreshTokensForUser(User user) {
        Instant now = Instant.now();
        List<RefreshToken> activeRefreshTokens = refreshTokenRepository
                .findAllByUserIdAndRevokedAtIsNullAndExpiresAtAfter(user.getId(), now);

        for (RefreshToken activeRefreshToken : activeRefreshTokens) {
            activeRefreshToken.setRevokedAt(now);
        }

        refreshTokenRepository.saveAll(activeRefreshTokens);
    }

    private String generateRefreshToken(){
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashRefreshToken(String rawRefreshToken) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(rawRefreshToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is not available", e);
        }
    }
}
