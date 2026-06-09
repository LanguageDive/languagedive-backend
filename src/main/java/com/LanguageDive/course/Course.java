package com.LanguageDive.course;

import com.LanguageDive.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String title;
    @Size(max = 255)
    private String description;
    @Size(max = 255)
    private String coverUrl;
    @NotNull
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;
    @NotNull
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }
}
