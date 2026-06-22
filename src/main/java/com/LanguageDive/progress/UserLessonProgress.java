package com.LanguageDive.progress;

import com.LanguageDive.content.Lesson;
import com.LanguageDive.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint( columnNames = {"user_id", "lesson_id"})
)
@NoArgsConstructor
public class UserLessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer lastReadingPosition;
    private Boolean completed = false;
    private Instant completedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

}
