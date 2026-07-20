package com.LanguageDive.auth.controller;

import com.LanguageDive.auth.dto.AuthResponse;
import com.LanguageDive.auth.dto.LoginRequest;
import com.LanguageDive.auth.dto.RefreshTokenRequest;
import com.LanguageDive.auth.dto.RegisterRequest;
import com.LanguageDive.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "Registro, inicio de sesión y gestión de tokens de acceso")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear cuenta de lector",
            description = """
                    Registra un nuevo usuario en LanguageDive. Cada lector obtiene su grimorio personal
                    donde guardará las palabras que descubra durante la lectura.

                    El username debe ser único. El email también debe ser único en el sistema.
                    La contraseña debe tener al menos 8 caracteres.

                    Devuelve los datos del usuario + tokens de acceso para arrancar a usar la API inmediatamente.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (username/email ya existe, email mal formado, contraseña muy corta)")
    })
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = """
                    Autentica al usuario con su username o email + contraseña.
                    Devuelve un par de tokens:
                    - **accessToken**: dura 15 minutos, se usa para autenticar todas las requests (via header Authorization)
                    - **refreshToken**: dura 7 días, permite renovar el accessToken sin pedir la contraseña otra vez
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas (usuario/email no existe o contraseña incorrecta)")
    })
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Renovar tokens",
            description = """
                    Obtiene un nuevo par de tokens (access + refresh) usando el refreshToken actual.
                    Útil cuando el accessToken expiró y querés evitar que el usuario tenga que loguearse de nuevo.

                    El refreshToken anterior queda invalidado automáticamente (rotation).
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renovados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Refresh token inválido o expirado")
    })
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Cerrar sesión",
            description = """
                    Invalida el refreshToken activo. El accessToken actual sigue siendo válido hasta que expire,
                    pero el usuario no podrá renovarlo sin iniciar sesión de nuevo.

                    Llamá a este endpoint cuando el usuario cierre sesión explícitamente.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sesión cerrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Refresh token inválido")
    })
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
    }
}
