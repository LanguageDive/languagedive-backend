# LanguageDive

**Aprende idiomas leyendo lo que te gusta.**

LanguageDive es una plataforma de lectura inmersiva que convierte libros reales (EPUB) en una experiencia de aprendizaje de idiomas. Olvídate de los ejercicios repetitivos y las listas de vocabulario fuera de contexto: acá leés historias, cuentos, novelas — lo que quieras — mientras construís tu vocabulario de forma orgánica.

---

## El problema

Aprender un idioma nuevo requiere **exposición constante a contenido real**. Pero los métodos tradicionales te dan:

- Libros de texto con diálogos artificiales
- Listas de vocabulario aisladas que olvidás a los tres días
- Ejercicios de gramática que no se parecen a una conversación real

Leer contenido auténtico es la mejor forma de adquirir un idioma, pero es frustrante tener que parar cada dos palabras para buscar su significado, perder el hilo, y no tener un sistema para recordar lo que aprendiste.

## La solución

LanguageDive te deja **importar cualquier libro en EPUB** y lo convierte en una experiencia de lectura diseñada para aprender:

| Problema | Solución LanguageDive |
|---|---|
| No entendés una palabra | La tocás y la guardás al instante en tu grimorio personal |
| Te olvidás lo que aprendiste | El grimorio registra tu progreso: nueva → aprendida → dominada |
| Perdés el hilo de la lectura | El progreso se guarda automáticamente por oración |
| Los libros de texto son aburridos | Leé lo que te gusta, en el idioma que querés aprender |

No es una app de ejercicios. Es un **lector que te ayuda a aprender**.

## Cómo funciona

```
1. Subís un EPUB → 2. Se divide en oraciones → 3. Leés oración por oración
                        ↓                              ↓
                 Se detecta el idioma         Tocás palabras → se guardan
                 automáticamente              en tu grimorio personal
```

- **Importación automática**: Subís un EPUB y el sistema lo parsea, detecta el idioma, y lo organiza en lecciones (una por capítulo) con oraciones individuales.
- **Lectura paginada**: Las oraciones se muestran paginadas. El frontend elige cuántas oraciones ver por página.
- **Vocabulario contextual**: Cuando encontrás una palabra que no conocés, la guardás con su traducción. Después podés repasarla desde tu grimorio.
- **Progreso persistente**: Cada lección guarda en qué oración te quedaste. Volvé cuando quieras y seguí desde ahí.
- **Traducción flexible**: Una palabra puede tener múltiples significados, separados por punto y coma. Ej: "marvel → maravillarse, asombrarse"

## El stack técnico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0 |
| Build | Maven Wrapper |
| Base de datos | PostgreSQL 17 |
| Migraciones | Flyway |
| Autenticación | Spring Security + JWT + Refresh Tokens |
| Concurrencia | Virtual Threads (Project Loom) |
| Documentación API | SpringDoc OpenAPI (Swagger UI) |
| Contenedor | Docker + Docker Compose |
| Proxy | Caddy (TLS automático) |

## Primeros pasos

### Requisitos

- Docker y Docker Compose
- Java 25 (para desarrollo local)

### Con Docker (recomendado)

```bash
docker compose up
```

La API arranca en `http://localhost:8080`. La documentación Swagger en `/swagger-ui/index.html`.

### Desarrollo local

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Producción

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Requiere un archivo `.env` con las variables de entorno de producción (DB, JWT secret, etc.).

## API

La documentación interactiva está disponible en `/swagger-ui/index.html` cuando el servidor está corriendo.

Endpoints principales:

- `POST /api/auth/register` — Crear cuenta de lector
- `POST /api/auth/login` — Iniciar sesión
- `GET /api/courses` — Ver tu estante de libros
- `POST /api/courses/import` — Importar un EPUB
- `GET /api/courses/{id}/lessons/{id}` — Leer una lección (oraciones paginadas)
- `PUT /api/progress/lessons/{id}` — Guardar progreso de lectura
- `GET /api/vocabulary` — Consultar tu grimorio personal
- `POST /api/vocabulary` — Guardar una palabra

## Estado del proyecto

Activo en desarrollo. Actualmente soporta importación de EPUB, lectura oración por oración, vocabulario contextual con estados de aprendizaje, y seguimiento de progreso.

## Licencia

MIT
