package com.LanguageDive.progress.entity;

import com.LanguageDive.content.entity.Course;
import com.LanguageDive.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
)
public class UserCourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer completedLessons;
    private Integer totalLessons;
    private Integer progressPercentage;
    private Instant lastAccessedAt;
    private Integer totalSentences;
    private Integer completedSentences;
    private Integer vocabularyKnown;
    private Integer vocabularyLearning;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
