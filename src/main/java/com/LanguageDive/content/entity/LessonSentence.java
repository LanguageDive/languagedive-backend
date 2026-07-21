package com.LanguageDive.content.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"lesson_id", "sentence_index"}))
public class LessonSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer sentenceIndex;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    public LessonSentence(Integer sentenceIndex, String text, Lesson lesson) {
        this.sentenceIndex = sentenceIndex;
        this.text = text;
        this.lesson = lesson;
    }
}
