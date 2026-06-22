package com.LanguageDive.content;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseLessonSummaryResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.CourseProgressResponse;
import com.LanguageDive.content.dto.LessonProgressResponse;
import com.LanguageDive.progress.UserCourseProgress;
import com.LanguageDive.progress.UserCourseProgressRepository;
import com.LanguageDive.progress.UserLessonProgress;
import com.LanguageDive.progress.UserLessonProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserCourseProgressRepository userCourseProgressRepository;
    private final UserLessonProgressRepository userLessonProgressRepository;

    @Transactional(readOnly = true)
    public List<CourseListResponse> getAllCoursesByUserId(Long userId) {
        List<Course> courses = courseRepository.findAllByUserId(userId);
        return courses.stream()
                .map(course -> CourseListResponse.from(course, getCourseProgress(userId, course.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(Long courseId, Long userId) {
        Course course = courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        List<Lesson> lessons = lessonRepository.findAllByCourseIdOrderByLessonOrderAsc(course.getId());
        Map<Long, UserLessonProgress> progressByLessonId = getLessonProgressByLessonId(userId, course.getId());

        List<CourseLessonSummaryResponse> lessonResponses = lessons.stream()
                .map(lesson -> CourseLessonSummaryResponse.from(
                        lesson,
                        toLessonProgressResponse(progressByLessonId.get(lesson.getId()))
                ))
                .toList();

        return CourseDetailResponse.from(
                course,
                getCourseProgress(userId, course.getId()),
                lessonResponses
        );
    }

    private CourseProgressResponse getCourseProgress(Long userId, Long courseId) {
        Optional<UserCourseProgress> progress = userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);
        return progress.map(CourseProgressResponse::from).orElse(null);
    }

    private Map<Long, UserLessonProgress> getLessonProgressByLessonId(Long userId, Long courseId) {
        List<UserLessonProgress> lessonProgresses = userLessonProgressRepository
                .findAllByUserIdAndLessonCourseId(userId, courseId);

        Map<Long, UserLessonProgress> progressByLessonId = new HashMap<>();
        for (UserLessonProgress lessonProgress : lessonProgresses) {
            progressByLessonId.put(lessonProgress.getLesson().getId(), lessonProgress);
        }
        return progressByLessonId;
    }

    private LessonProgressResponse toLessonProgressResponse(UserLessonProgress progress) {
        if (progress == null) {
            return null;
        }

        return LessonProgressResponse.from(progress);
    }
}
