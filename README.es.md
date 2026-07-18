# LanguageDive Backend

API backend para una plataforma de aprendizaje de idiomas. Gestiona cursos, lecciones, sesiones de lectura, vocabulario personal y seguimiento de progreso del usuario.

Construido con Spring Boot 4 y PostgreSQL.

## Funcionalidades

- **Autenticación y Autorización**: Registro, inicio de sesión, tokens JWT, rotación de refresh tokens, cierre de sesión
- **Cursos y Lecciones**: Crear y gestionar cursos de idiomas con lecciones ordenadas y conteo de palabras
- **Seguimiento de Progreso**: Registrar finalización de lecciones, posición de lectura y progreso general del curso
- **Vocabulario**: Listas de vocabulario personal con seguimiento de estado (aprendiendo, repasando, dominado)
- **Sesiones de Lectura**: Seguimiento de sesiones de lectura por lección basado en tiempo

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Build | Maven Wrapper (`./mvnw`) |
| Base de datos | PostgreSQL 18 |
| Migraciones | Flyway |
| Autenticación | Spring Security + JWT (jjwt 0.13.0) + Refresh Tokens |
| DTOs | Java Records |
| Contenedor | Docker Compose |

## Cómo Empezar

### Requisitos

- Docker y Docker Compose
- Java 25 (para desarrollo sin Docker)

### Ejecutar con Docker (recomendado)

```bash
docker compose up
```

Esto inicia PostgreSQL 18 y el servidor API con live-reload en el puerto `8080`. Puerto de debug disponible en `8000`.

### Ejecutar localmente

```bash
# Iniciar PostgreSQL primero, luego:
export POSTGRES_URL=jdbc:postgresql://localhost:5432/languagedive
export POSTGRES_USER=languagedive
export POSTGRES_PASSWORD=languagedive
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Compilar

```bash
./mvnw compile       # Solo compilar
./mvnw package       # Generar JAR ejecutable
```

## Resumen de la API

Todos los endpoints usan el prefijo `/api`. Rutas públicas: `/api/auth/**`. El resto requiere autenticación mediante token JWT (Bearer).

### Autenticación

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registrar un nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión con usuario/email + contraseña |
| POST | `/api/auth/refresh` | Rotar refresh token |
| POST | `/api/auth/logout` | Revocar refresh token |

### Cursos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/courses` | Listar cursos del usuario |
| GET | `/api/courses/{id}` | Obtener detalle del curso con lecciones |
| POST | `/api/courses` | Crear un nuevo curso |

### Lecciones

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/lessons/{id}` | Obtener detalle de la lección |
| POST | `/api/lessons` | Crear una lección en un curso |

### Progreso

| Método | Ruta | Descripción |
|--------|------|-------------|
| PUT | `/api/progress/lessons/{lessonId}` | Actualizar posición de lectura y finalización |

### Vocabulario

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/vocabulary` | Listar entradas de vocabulario del usuario |
| POST | `/api/vocabulary` | Crear o actualizar una entrada (upsert) |
| PATCH | `/api/vocabulary/{id}` | Actualizar una entrada específica |

Nota: POST `/api/vocabulary` retorna `201 Created` para entradas nuevas y `200 OK` para actualizaciones.

## Estructura del Proyecto

```
src/main/java/com/LanguageDive/
├── auth/               # Autenticación y gestión de usuarios
│   ├── controller/
│   ├── dto/
│   ├── entity/         # User, RefreshToken
│   ├── repository/
│   ├── security/       # Filtro JWT, UserPrincipal
│   └── service/
├── content/            # Cursos y lecciones
│   ├── dto/
│   └── ...
├── progress/           # Progreso de lecciones/cursos, sesiones de lectura
│   ├── dto/
│   └── ...
├── vocabulary/         # Vocabulario personal
│   ├── dto/
│   └── ...
├── config/             # Configuración de seguridad
└── common/
    └── exception/      # Manejo global de errores (ProblemDetail)
```

## Manejo de Errores

Todos los errores siguen el estándar [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807), retornando respuestas JSON estructuradas.

## Base de Datos

- Schema gestionado por migraciones Flyway (`src/main/resources/db/migration/`)
- Tablas: `users`, `course`, `lesson`, `user_course_progress`, `user_lesson_progress`, `vocabulary_entry`, `reading_session`, `refresh_token`

## Convención de Commits

Este proyecto usa commits convencionales:

```
feat: nueva funcionalidad
fix: corrección de bug
refactor: reestructuración de código
chore: herramientas, dependencias, configuración
docs: documentación
```
