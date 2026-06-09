package com.LanguageDive.user;

import com.LanguageDive.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    //helpers
    public void addCourse(Course course){
        courses.add(course);
        course.setUser(this);
    }

    public void removeCourse(Course course){
        courses.remove(course);
        course.setUser(null);
    }

}
