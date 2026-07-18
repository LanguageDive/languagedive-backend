package com.LanguageDive.content.service;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.entity.Course;
import com.LanguageDive.content.entity.Lesson;
import com.LanguageDive.content.repository.LessonRepository;
import com.LanguageDive.content.dto.CreateLessonRequest;
import com.LanguageDive.content.dto.LessonProgressResponse;
import com.LanguageDive.content.dto.LessonResponse;
import com.LanguageDive.progress.entity.UserLessonProgress;
import com.LanguageDive.progress.repository.UserLessonProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserLessonProgressRepository userLessonProgressRepository;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public Lesson getOwnedLessonOrThrow(Long lessonId, Long userId) {
        return lessonRepository.findByIdAndCourseUserId(lessonId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));
    }

    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Long lessonId, Long userId) {
        Lesson lesson = getOwnedLessonOrThrow(lessonId, userId);

        UserLessonProgress progress = userLessonProgressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .orElse(null);

        LessonProgressResponse progressResponse = progress == null ? null : LessonProgressResponse.from(progress);
        return LessonResponse.from(lesson, progressResponse);
    }

    @Transactional
    public LessonResponse createLesson(Long userId, CreateLessonRequest request) {
        Course course = courseService.getOwnedCourseOrThrow(request.courseId(), userId);

        Lesson lesson = new Lesson();
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setLessonOrder(request.lessonOrder());
        lesson.setWordCount(resolveWordCount(request));
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        return LessonResponse.from(savedLesson, null);
    }

    private Integer resolveWordCount(CreateLessonRequest request) {
        if (request.wordCount() != null) {
            return request.wordCount();
        }

        String normalizedContent = request.content().trim();
        if (normalizedContent.isEmpty()) {
            return 0;
        }

        return normalizedContent.split("\\s+").length;
    }
}
