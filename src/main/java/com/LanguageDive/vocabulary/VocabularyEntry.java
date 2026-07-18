package com.LanguageDive.vocabulary;

import com.LanguageDive.auth.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(name = "vocabulary_entry", uniqueConstraints = @UniqueConstraint(columnNames = {"term", "user_id"}))
public class VocabularyEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String term;
    @NotBlank
    @Size(max = 255)
    private String meaning;
    @NotNull
    @Enumerated(EnumType.STRING)
    private VocabularyStatus status;
    @NotNull
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastSeenAt;
    private Integer timesSeen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void onCreate(){
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = Instant.now();
    }
}
