package com.LanguageDive.lesson;

import com.LanguageDive.course.Course;
import com.LanguageDive.progress.UserLessonProgress;
import com.LanguageDive.reading.ReadingSession;
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
@Setter
@Getter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Integer lessonOrder;
    private Integer wordCount;
    @NotNull
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadingSession> readingSessions = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLessonProgress> userLessonProgresses = new ArrayList<>();

    @PrePersist
    public void onCreate(){
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = Instant.now();
    }

    // helpers

    public void addReadingSession(ReadingSession readingSession){
        readingSessions.add(readingSession);
        readingSession.setLesson(this);
    }

    public void removeReadingSession(ReadingSession readingSession){
        readingSessions.remove(readingSession);
        readingSession.setLesson(null);
    }

    public void addUserLessonProgress(UserLessonProgress userLessonProgress){
        userLessonProgresses.add(userLessonProgress);
        userLessonProgress.setLesson(this);
    }

    public void removeUserLessonProgress(UserLessonProgress userLessonProgress){
        userLessonProgresses.remove(userLessonProgress);
        userLessonProgress.setLesson(null);
    }
}
