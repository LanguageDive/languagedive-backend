package com.LanguageDive.progress;

import com.LanguageDive.content.Lesson;
import com.LanguageDive.content.LessonService;
import com.LanguageDive.progress.dto.UpdateLessonProgressRequest;
import com.LanguageDive.progress.dto.UpdateLessonProgressResponse;
import com.LanguageDive.user.User;
import com.LanguageDive.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserLessonProgressService {

    private final UserLessonProgressRepository userLessonProgressRepository;
    private final UserService userService;
    private final LessonService lessonService;

    @Transactional
    public UpdateLessonProgressResponse updateProgress(Long userId, Long lessonId, UpdateLessonProgressRequest dto){
        Lesson lesson = lessonService.getOwnedLessonOrThrow(lessonId, userId);
        Optional<UserLessonProgress> userLessonProgress = userLessonProgressRepository.findByUserIdAndLessonId(userId, lessonId);

        if(userLessonProgress.isEmpty()){
            UserLessonProgress newUserLessonProgress = new UserLessonProgress();
            User user = userService.findById(userId);
            newUserLessonProgress.setLesson(lesson);
            newUserLessonProgress.setUser(user);
            userLessonProgress = Optional.of(newUserLessonProgress);
        }

        UserLessonProgress userLessonEntity = userLessonProgress.get();
        userLessonEntity.setLastReadingPosition(dto.lastReadingPosition());
        if(dto.completed()){
            userLessonEntity.setCompleted(true);
            userLessonEntity.setCompletedAt(Instant.now());
        }

        userLessonProgressRepository.save(userLessonEntity);

        return UpdateLessonProgressResponse.toDto(userLessonEntity);
    }
}
