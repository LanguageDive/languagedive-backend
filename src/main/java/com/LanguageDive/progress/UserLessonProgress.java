package com.LanguageDive.progress;

import com.LanguageDive.lesson.Lesson;
import com.LanguageDive.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint( columnNames = {"user_id", "lesson_id"})
)
public class UserLessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer lastReadingPosition;
    private Boolean completed;
    private Instant completedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

}
