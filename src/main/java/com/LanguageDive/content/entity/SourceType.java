package com.LanguageDive.content.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato del contenido fuente del curso: EPUB (libro digital) o TXT (texto plano)")
public enum SourceType {
    EPUB,
    TXT
}
