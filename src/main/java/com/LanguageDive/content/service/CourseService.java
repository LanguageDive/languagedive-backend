package com.LanguageDive.content.service;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.dto.CreateCourseRequest;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseLessonSummaryResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.CourseProgressResponse;
import com.LanguageDive.content.dto.LessonProgressResponse;
import com.LanguageDive.progress.entity.UserCourseProgress;
import com.LanguageDive.progress.repository.UserCourseProgressRepository;
import com.LanguageDive.progress.entity.UserLessonProgress;
import com.LanguageDive.progress.repository.UserLessonProgressRepository;
import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.service.UserService;
import com.LanguageDive.content.entity.Course;
import com.LanguageDive.content.entity.Lesson;
import com.LanguageDive.content.repository.CourseRepository;
import com.LanguageDive.content.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<CourseListResponse> getAllCoursesByUserId(Long userId) {
        List<Course> courses = courseRepository.findAllByUserId(userId);
        return courses.stream()
                .map(course -> CourseListResponse.from(course, getCourseProgress(userId, course.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(Long courseId, Long userId) {
        Course course = getOwnedCourseOrThrow(courseId, userId);

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

    @Transactional(readOnly = true)
    public Course getOwnedCourseOrThrow(Long courseId, Long userId) {
        return courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    @Transactional
    public CourseDetailResponse createCourse(Long userId, CreateCourseRequest request) {
        User user = userService.findById(userId);

        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCoverUrl(request.coverUrl());
        course.setSourceType(request.sourceType());
        course.setUser(user);

        Course savedCourse = courseRepository.save(course);
        return CourseDetailResponse.from(savedCourse, null, Collections.emptyList());
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
