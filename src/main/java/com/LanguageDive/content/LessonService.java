package com.LanguageDive.content;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.dto.LessonProgressResponse;
import com.LanguageDive.content.dto.LessonResponse;
import com.LanguageDive.progress.UserLessonProgress;
import com.LanguageDive.progress.UserLessonProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserLessonProgressRepository userLessonProgressRepository;

    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Long lessonId, Long userId) {
        Lesson lesson = lessonRepository.findByIdAndCourseUserId(lessonId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));

        UserLessonProgress progress = userLessonProgressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .orElse(null);

        LessonProgressResponse progressResponse = progress == null ? null : LessonProgressResponse.from(progress);
        return LessonResponse.from(lesson, progressResponse);
    }
}
