package com.LanguageDive.content.service;

import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.service.UserService;
import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseLessonSummaryResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import com.LanguageDive.content.dto.CourseProgressResponse;
import com.LanguageDive.content.dto.CreateCourseRequest;
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
import com.LanguageDive.vocabulary.entity.VocabularyEntry;
import com.LanguageDive.vocabulary.repository.VocabularyEntryRepository;
import java.util.Collections;
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
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final UserService userService;

    private static final int DEFAULT_PAGE_SIZE = 10;

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
    public CourseDetailResponse createCourse(Long userId, CreateCourseRequest request) {
        User user = userService.findById(userId);

        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setUser(user);

        Course savedCourse = courseRepository.save(course);
        return CourseDetailResponse.from(savedCourse, null, Collections.emptyList());
    }

    @Transactional
    public ImportCourseResponse importEpub(Long userId, MultipartFile file, String description) throws Exception {
        EpubParser epubParser = new EpubParser();
        EpubParser.Resultado resultado = epubParser.parsear(file.getInputStream());

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
                totalSentences
        );
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonPage(Long lessonId, Long userId, int page, int pageSize) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));

        long totalSentences = lessonSentenceRepository.countByLessonId(lessonId);
        int totalPages = (int) Math.ceil((double) totalSentences / pageSize);
        if (totalPages == 0) totalPages = 1;

        List<LessonSentence> allSentences = lessonSentenceRepository.findByLessonIdOrderBySentenceIndexAsc(lessonId);
        int fromIndex = page * pageSize;
        if (fromIndex > allSentences.size()) fromIndex = allSentences.size();
        int toIndex = Math.min(fromIndex + pageSize, allSentences.size());
        List<LessonSentence> pageSentences = allSentences.subList(fromIndex, toIndex);

        Map<String, VocabularyEntry> vocabulary = new HashMap<>();
        List<VocabularyEntry> entries = vocabularyEntryRepository.findAllByUserId(userId);
        for (VocabularyEntry entry : entries) {
            vocabulary.put(entry.getTerm(), entry);
        }

        List<SentenceResponse> sentenceResponses = pageSentences.stream()
                .map(s -> new SentenceResponse(s.getId(), s.getSentenceIndex(), s.getText()))
                .toList();

        Map<String, VocabularyEntryResponseMap> vocabMap = new HashMap<>();
        for (VocabularyEntry entry : entries) {
            vocabMap.put(entry.getTerm(), new VocabularyEntryResponseMap(
                    entry.getTerm(), entry.getTranslation(), entry.getTranslationLang(), entry.getStatus().name()
            ));
        }

        return new LessonDetailResponse(
                lesson.getId(), lesson.getTitle(),
                sentenceResponses, page, pageSize,
                (int) totalSentences, totalPages, vocabMap
        );
    }

    public EpubParser.Resultado obtenerMetadatosEpub(MultipartFile file) throws Exception {
        EpubParser epubParser = new EpubParser();
        return epubParser.parsear(file.getInputStream());
    }

    @Transactional
    public void updateLessonProgress(Long userId, Long lessonId, Integer sentenceIndex) {
        UserLessonProgress progress = userLessonProgressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> {
                    UserLessonProgress newProgress = new UserLessonProgress();
                    newProgress.setUser(userService.findById(userId));
                    newProgress.setLesson(lessonRepository.getReferenceById(lessonId));
                    newProgress.setCompleted(false);
                    return newProgress;
                });

        progress.setLastReadingPosition(sentenceIndex);
        userLessonProgressRepository.save(progress);
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

    public record VocabularyEntryResponseMap(
            String term, String translation, String translationLang, String status
    ) {}
}
