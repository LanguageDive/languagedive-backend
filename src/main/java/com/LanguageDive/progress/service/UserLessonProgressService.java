package com.LanguageDive.progress.service;

import com.LanguageDive.content.entity.Lesson;
import com.LanguageDive.content.service.LessonService;
import com.LanguageDive.progress.dto.UpdateLessonProgressRequest;
import com.LanguageDive.progress.dto.UpdateLessonProgressResponse;
import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.service.UserService;
import com.LanguageDive.progress.entity.UserLessonProgress;
import com.LanguageDive.progress.repository.UserLessonProgressRepository;
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
        userLessonEntity.setLastReadingPosition(dto.sentenceIndex());
        if(dto.completed()){
            userLessonEntity.setCompleted(true);
            userLessonEntity.setCompletedAt(Instant.now());
        }

        userLessonProgressRepository.save(userLessonEntity);

        return UpdateLessonProgressResponse.toDto(userLessonEntity);
    }
}
