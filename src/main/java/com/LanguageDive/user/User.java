package com.LanguageDive.user;

import com.LanguageDive.content.Course;
import com.LanguageDive.progress.UserCourseProgress;
import com.LanguageDive.progress.UserLessonProgress;
import com.LanguageDive.reading.ReadingSession;
import com.LanguageDive.vocabulary.VocabularyEntry;
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
    @Column(unique = true)
    private String username;
    @NotBlank
    @Size(max = 255)
    @Column(unique = true)
    private String email;
    @NotBlank
    private String passwordHash;
    @NotNull
    private Instant createdAt;
    private Instant updatedAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VocabularyEntry> vocabularyEntries = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadingSession> readingSessions = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLessonProgress> userLessonProgresses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourseProgress> userCourseProgresses = new ArrayList<>();

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

    public void addVocabularyEntry(VocabularyEntry vocabularyEntry){
        vocabularyEntries.add(vocabularyEntry);
        vocabularyEntry.setUser(this);
    }

    public void removeVocabularyEntry(VocabularyEntry vocabularyEntry){
        vocabularyEntries.remove(vocabularyEntry);
        vocabularyEntry.setUser(null);
    }

    public void addReadingSession(ReadingSession readingSession){
        readingSessions.add(readingSession);
        readingSession.setUser(this);
    }

    public void removeReadingSession(ReadingSession readingSession){
        readingSessions.remove(readingSession);
        readingSession.setUser(null);
    }

    public void addUserLessonProgress(UserLessonProgress ulp){
        userLessonProgresses.add(ulp);
        ulp.setUser(this);
    }
    public void removeUserLessonProgress(UserLessonProgress ulp){
        userLessonProgresses.remove(ulp);
        ulp.setUser(null);
    }

    public void addUserCourseProgress(UserCourseProgress ucp){
        userCourseProgresses.add(ucp);
        ucp.setUser(this);
    }
    public void removeUserCourseProgress(UserCourseProgress ucp){
        userCourseProgresses.remove(ucp);
        ucp.setUser(null);
    }
}
