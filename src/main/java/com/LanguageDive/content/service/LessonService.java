package com.LanguageDive.content.service;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.entity.Lesson;
import com.LanguageDive.content.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    public Lesson getOwnedLessonOrThrow(Long lessonId, Long userId) {
        return lessonRepository.findByIdAndCourseUserId(lessonId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));
    }
}
