package com.LanguageDive.content;

import com.LanguageDive.progress.UserCourseProgress;
import com.LanguageDive.auth.entity.User;
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
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourseProgress> userCourseProgresses = new ArrayList<>();

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    //helpers

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
        lesson.setCourse(this);
    }

    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
        lesson.setCourse(null);
    }

    public void addUserCourseProgress(UserCourseProgress userCourseProgress) {
        userCourseProgresses.add(userCourseProgress);
        userCourseProgress.setCourse(this);
    }

    public void removeUserCourseProgress(UserCourseProgress userCourseProgress){
        userCourseProgresses.remove(userCourseProgress);
        userCourseProgress.setCourse(null);
    }
}
