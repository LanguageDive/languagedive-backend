package com.LanguageDive.vocabulary.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = """
        Estado de aprendizaje de una palabra en el grimorio del lector.
        Progresión: NEW → LEVEL_2 → LEVEL_3 → LEVEL_4 → LEVEL_5 → LEARNED.
        IGNORED: el usuario descartó la palabra.
        """)
public enum VocabularyStatus {
    NEW,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    LEVEL_5,
    LEARNED,
    IGNORED
}
