package com.LanguageDive.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LanguageDive API",
                version = "0.1.0",
                description = """
                        API del grimorio de lectura inmersiva. LanguageDive permite leer textos en otros idiomas
                        mientras construís tu vocabulario de forma orgánica.

                        ## Flujo típico
                        1. **Registrate** → crea tu cuenta de lector
                        2. **Creá un curso** → agregá un libro (EPUB o TXT) a tu estante
                        3. **Leé** → abrí las lecciones y toca las palabras que no conozcas
                        4. **Construí tu grimorio** → las palabras que guardás aparecen en tu vocabulario personal
                        5. **Seguí tu progreso** → cada lección guarda tu avance

                        ## Autenticación
                        La mayoría de los endpoints requieren un token JWT. Obtenelo via `/api/auth/login` o `/api/auth/register`
                        y usa el botón **Authorize** (arriba a la derecha) con el `accessToken`.
                        """,
                contact = @Contact(name = "LanguageDive Team"),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Desarrollo local")
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Copiá el accessToken que devuelve login/register y pegalo acá (sin el prefijo Bearer)"
)
public class OpenAPIConfig {
}
