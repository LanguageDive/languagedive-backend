package com.LanguageDive.user;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String username;
    @NotBlank
    @Size(max = 255)
    private String email;
    @NotBlank
    private String passwordHash;
    @NotNull
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

}
