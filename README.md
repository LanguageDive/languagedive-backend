# LanguageDive

Aprender un idioma nuevo es difícil. No porque sea imposible, sino porque los métodos que usamos están diseñados para todo menos para lo que funciona: leer contenido real.

Los libros de texto te dan diálogos que nadie dice en la vida real. Las apps te hacen repetir palabras sueltas hasta que las memorizás para el examen, pero una semana después ya no te acordás. Y buscar cada palabra que no entendés en un diccionario mientras leés es tan frustrante que terminás abandonando.

LanguageDive nace de una idea simple: **la mejor forma de aprender un idioma es leer cosas que te interesen, con herramientas que no se interpongan en el camino.**

## Cómo funciona

Subís un libro en EPUB. El sistema lo divide en oraciones, detecta el idioma automáticamente, y lo organiza en lecciones — una por capítulo. Después leés oración por oración, a tu ritmo.

Cuando encontrás una palabra que no conocés, la tocás y al instante queda guardada en tu grimorio personal con su traducción. No necesitás salir de la lectura, no perdés el hilo. La palabra queda registrada con su estado de aprendizaje: nueva, en práctica, aprendida, ignorada.

El progreso se guarda automáticamente. Cerraste la página? Volvé después y seguís exactamente desde la oración donde te quedaste.

No es una app de ejercicios. Es un lector que te ayuda a aprender mientras hacés lo que ya funciona: leer.

## Lo que podés hacer hoy

- Importar cualquier libro en EPUB — el sistema lo parsea, detecta el idioma y lo deja listo para leer
- Leer oración por oración con paginación ajustable
- Guardar palabras con su traducción al instante
- Consultar tu grimorio personal con el estado de cada palabra
- Seguir tu progreso por lección y por curso
- Todo sincronizado, todo persistente

## Stack técnico

Java 25, Spring Boot 4.0, PostgreSQL 17, Flyway, Docker. La API corre con virtual threads para manejar concurrencia sin necesidad de código reactivo.

## Cómo arrancar

Con Docker:
```bash
docker compose up
```

La API queda en `http://localhost:8080` y la documentación interactiva en `/swagger-ui/index.html`.

Sin Docker:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Producción:
```bash
docker compose -f docker-compose.prod.yml up -d --build
```

## Licencia

MIT

---

*English version: [`README.en.md`](./README.en.md)*
