package com.LanguageDive.content.service;

import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.service.UserService;
import com.LanguageDive.common.exception.FileProcessingException;
import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseLessonSummaryResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.CourseProgressResponse;
import com.LanguageDive.content.dto.ImportCourseResponse;
import com.LanguageDive.content.dto.LessonDetailResponse;
import com.LanguageDive.content.dto.LessonProgressResponse;
import com.LanguageDive.content.dto.SentenceResponse;
import com.LanguageDive.content.entity.Course;
import com.LanguageDive.content.entity.Lesson;
import com.LanguageDive.content.entity.LessonSentence;
import com.LanguageDive.content.entity.SourceType;
import com.LanguageDive.content.repository.CourseRepository;
import com.LanguageDive.content.repository.LessonRepository;
import com.LanguageDive.content.repository.LessonSentenceRepository;
import com.LanguageDive.progress.entity.UserCourseProgress;
import com.LanguageDive.progress.entity.UserLessonProgress;
import com.LanguageDive.progress.repository.UserCourseProgressRepository;
import com.LanguageDive.progress.repository.UserLessonProgressRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonSentenceRepository lessonSentenceRepository;
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
                        lesson, toLessonProgressResponse(progressByLessonId.get(lesson.getId()))))
                .toList();

        return CourseDetailResponse.from(course, getCourseProgress(userId, course.getId()), lessonResponses);
    }

    @Transactional(readOnly = true)
    public Course getOwnedCourseOrThrow(Long courseId, Long userId) {
        return courseRepository
                .findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    @Transactional
    public ImportCourseResponse importEpub(Long userId, MultipartFile file, String description) {
        EpubParser epubParser = new EpubParser();
        EpubParser.Resultado resultado;
        try {
            resultado = epubParser.parsear(file.getInputStream());
        } catch (Exception e) {
            throw new FileProcessingException("No se pudo procesar el archivo: " + e.getMessage(), e);
        }

        User user = userService.findById(userId);

        Course course = new Course();
        course.setTitle(resultado.titulo());
        course.setDescription(description);
        course.setSourceType(SourceType.EPUB);
        course.setLanguage(resultado.idioma());
        course.setUser(user);
        course = courseRepository.save(course);

        int totalSentences = 0;
        for (EpubParser.Capitulo capitulo : resultado.capitulos()) {
            Lesson lesson = new Lesson();
            lesson.setTitle(capitulo.titulo());
            lesson.setContent(capitulo.contenido());
            lesson.setLessonOrder(capitulo.orden());
            lesson.setWordCount(capitulo.oraciones().size());
            lesson.setCourse(course);
            lesson = lessonRepository.save(lesson);

            for (EpubParser.Sentence oracion : capitulo.oraciones()) {
                LessonSentence sentence = new LessonSentence(oracion.index(), oracion.text(), lesson);
                lessonSentenceRepository.save(sentence);
            }

            totalSentences += capitulo.oraciones().size();
        }

        UserCourseProgress progress = new UserCourseProgress();
        progress.setUser(user);
        progress.setCourse(course);
        progress.setCompletedLessons(0);
        progress.setTotalLessons(resultado.capitulos().size());
        progress.setProgressPercentage(0);
        progress.setTotalSentences(totalSentences);
        progress.setCompletedSentences(0);
        progress.setVocabularyKnown(0);
        progress.setVocabularyLearning(0);
        userCourseProgressRepository.save(progress);

        return new ImportCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLanguage(),
                resultado.capitulos().size(),
                totalSentences);
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonPage(Long lessonId, Long userId, int page, int pageSize) {
        Lesson lesson = lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));

        long totalSentences = lessonSentenceRepository.countByLessonId(lessonId);
        int totalPages = (int) Math.ceil((double) totalSentences / pageSize);
        if (totalPages == 0) totalPages = 1;

        List<LessonSentence> allSentences = lessonSentenceRepository.findByLessonIdOrderBySentenceIndexAsc(lessonId);
        int fromIndex = page * pageSize;
        if (fromIndex > allSentences.size()) fromIndex = allSentences.size();
        int toIndex = Math.min(fromIndex + pageSize, allSentences.size());
        List<SentenceResponse> sentenceResponses = allSentences.subList(fromIndex, toIndex).stream()
                .map(s -> new SentenceResponse(s.getId(), s.getSentenceIndex(), s.getText()))
                .toList();

        return new LessonDetailResponse(
                lesson.getId(), lesson.getTitle(), sentenceResponses, page, pageSize, (int) totalSentences, totalPages);
    }

    private CourseProgressResponse getCourseProgress(Long userId, Long courseId) {
        Optional<UserCourseProgress> progress = userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);
        return progress.map(CourseProgressResponse::from).orElse(null);
    }

    private Map<Long, UserLessonProgress> getLessonProgressByLessonId(Long userId, Long courseId) {
        List<UserLessonProgress> lessonProgresses =
                userLessonProgressRepository.findAllByUserIdAndLessonCourseId(userId, courseId);

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
