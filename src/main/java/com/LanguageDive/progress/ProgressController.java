package com.LanguageDive.progress;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.progress.dto.UpdateLessonProgressRequest;
import com.LanguageDive.progress.dto.UpdateLessonProgressResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/progress")
public class ProgressController {

    private final UserLessonProgressService userLessonProgressService;

    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<UpdateLessonProgressResponse> updateLessonProgressByUserIdAndLessonId(@PathVariable("lessonId") Long lessonId,
                                                                                                @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                                                @Valid  @RequestBody UpdateLessonProgressRequest updateLessonProgressRequest){
        UpdateLessonProgressResponse response = userLessonProgressService.updateProgress(userPrincipal.getUserId(), lessonId,  updateLessonProgressRequest);
        return ResponseEntity.ok(response);
    }
}
