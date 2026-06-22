package com.LanguageDive.content;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.dto.LessonResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getLessonById(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(lessonService.getLessonById(lessonId, userPrincipal.getUserId()));
    }
}
