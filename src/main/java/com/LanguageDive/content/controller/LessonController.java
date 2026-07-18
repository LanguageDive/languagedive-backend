package com.LanguageDive.content.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.service.LessonService;
import com.LanguageDive.content.dto.CreateLessonRequest;
import com.LanguageDive.content.dto.LessonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lessonId}")
    public LessonResponse getLessonById(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return lessonService.getLessonById(lessonId, userPrincipal.getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse createLesson(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateLessonRequest request
    ) {
        return lessonService.createLesson(userPrincipal.getUserId(), request);
    }
}
