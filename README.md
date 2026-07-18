# LanguageDive Backend

Language learning platform API — a backend service for managing courses, lessons, reading sessions, vocabulary, and user progress tracking.

Built with Spring Boot 4 and PostgreSQL.

## Features

- **Authentication & Authorization**: Register, login, JWT access tokens, refresh token rotation, logout
- **Courses & Lessons**: Create and manage language courses with ordered lessons and word counts
- **Progress Tracking**: Track lesson completion, reading position, and overall course progress
- **Vocabulary**: Personal vocabulary lists with status tracking (learning, reviewing, mastered)
- **Reading Sessions**: Time-based reading session tracking for each lesson

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Build | Maven Wrapper (`./mvnw`) |
| Database | PostgreSQL 18 |
| Migrations | Flyway |
| Auth | Spring Security + JWT (jjwt 0.13.0) + Refresh Tokens |
| DTOs | Java Records |
| Container | Docker Compose |

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 25 (for development without Docker)

### Run with Docker (recommended)

```bash
docker compose up
```

This starts PostgreSQL 18 and the API server with live-reload on port `8080`. Debug port available on `8000`.

### Run locally

```bash
# Start PostgreSQL first, then:
export POSTGRES_URL=jdbc:postgresql://localhost:5432/languagedive
export POSTGRES_USER=languagedive
export POSTGRES_PASSWORD=languagedive
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Build

```bash
./mvnw compile       # Compile only
./mvnw package       # Build executable JAR
```

## API Overview

All endpoints are prefixed with `/api`. Public routes: `/api/auth/**`. All other routes require authentication via Bearer JWT token.

### Auth

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login with username/email + password |
| POST | `/api/auth/refresh` | Rotate refresh token |
| POST | `/api/auth/logout` | Revoke refresh token |

### Courses

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/courses` | List user's courses |
| GET | `/api/courses/{id}` | Get course detail with lessons |
| POST | `/api/courses` | Create a new course |

### Lessons

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/lessons/{id}` | Get lesson detail |
| POST | `/api/lessons` | Create a lesson in a course |

### Progress

| Method | Path | Description |
|--------|------|-------------|
| PUT | `/api/progress/lessons/{lessonId}` | Update lesson reading position and completion |

### Vocabulary

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/vocabulary` | List user's vocabulary entries |
| POST | `/api/vocabulary` | Create or update a vocabulary entry (upsert) |
| PATCH | `/api/vocabulary/{id}` | Update a specific vocabulary entry |

Note: POST `/api/vocabulary` returns `201 Created` for new entries and `200 OK` for updates.

## Project Structure

```
src/main/java/com/LanguageDive/
├── auth/               # Authentication & user management
│   ├── controller/
│   ├── dto/
│   ├── entity/         # User, RefreshToken
│   ├── repository/
│   ├── security/       # JWT filter, UserPrincipal
│   └── service/
├── content/            # Courses & lessons
│   ├── dto/
│   └── ...
├── progress/           # Lesson/course progress, reading sessions
│   ├── dto/
│   └── ...
├── vocabulary/         # Personal vocabulary
│   ├── dto/
│   └── ...
├── config/             # Security configuration
└── common/
    └── exception/      # Global error handling (ProblemDetail)
```

## Error Handling

All errors follow the [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807) standard, returning structured JSON error responses.

## Database

- Schema managed by Flyway migrations (`src/main/resources/db/migration/`)
- Tables: `users`, `course`, `lesson`, `user_course_progress`, `user_lesson_progress`, `vocabulary_entry`, `reading_session`, `refresh_token`

## Commit Convention

This project uses conventional commits:

```
feat: new feature
fix: bug fix
refactor: code restructuring
chore: tooling, dependencies, config
docs: documentation
```
