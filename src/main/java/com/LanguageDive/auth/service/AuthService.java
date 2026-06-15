package com.LanguageDive.auth.service;

import com.LanguageDive.auth.dto.*;
import com.LanguageDive.auth.entity.RefreshToken;
import com.LanguageDive.common.exception.InvalidCredentialsException;
import com.LanguageDive.common.exception.UserAlreadyExistsException;
import com.LanguageDive.user.User;
import com.LanguageDive.user.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedUsername = request.username().trim();
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new UserAlreadyExistsException("USERNAME_ALREADY_EXISTS", "Username already exists");
        }

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new UserAlreadyExistsException("EMAIL_ALREADY_EXISTS", "Email already exists");
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);
        RefreshTokenIssuance refreshTokenIssuance = refreshTokenService.createRefreshToken(savedUser);

        return buildAuthResponse(savedUser, refreshTokenIssuance.rawToken());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.identifier().trim(), request.password())
            );
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        request.identifier().trim(),
                        request.identifier().trim()
                )
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        RefreshTokenIssuance refreshTokenIssuance = refreshTokenService.createRefreshToken(user);

        return buildAuthResponse(user, refreshTokenIssuance.rawToken());
    }

    private AuthResponse buildAuthResponse(User user, String refreshToken) {
        AuthUserResponse authUserResponse = new AuthUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );

        return new AuthResponse(
                authUserResponse,
                jwtService.getToken(user),
                refreshToken
        );
    }

    public AuthResponse refresh(@Valid RefreshTokenRotationRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        User user = refreshToken.getUser();

        refreshTokenService.revokeRefreshToken(refreshToken);
        RefreshTokenIssuance refreshTokenIssuance = refreshTokenService.createRefreshToken(user);
        return buildAuthResponse(user, refreshTokenIssuance.rawToken());
    }
}
