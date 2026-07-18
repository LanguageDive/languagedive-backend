package com.LanguageDive.content.controller;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.service.CourseService;
import com.LanguageDive.content.dto.CreateCourseRequest;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseListResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    public List<CourseListResponse> getCoursesByUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        Long userId = userPrincipal.getUserId();
        return courseService.getAllCoursesByUserId(userId);
    }

    @GetMapping("/{courseId}")
    public CourseDetailResponse getCourseById(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return courseService.getCourseById(courseId, userPrincipal.getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDetailResponse createCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateCourseRequest request
    ) {
        return courseService.createCourse(userPrincipal.getUserId(), request);
    }

}
