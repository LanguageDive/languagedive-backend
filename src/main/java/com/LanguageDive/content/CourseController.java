package com.LanguageDive.content;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.content.dto.CreateCourseRequest;
import com.LanguageDive.content.dto.CourseDetailResponse;
import com.LanguageDive.content.dto.CourseListResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    public ResponseEntity<List<CourseListResponse>> getCoursesByUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        Long userId = userPrincipal.getUserId();
        return ResponseEntity.ok(courseService.getAllCoursesByUserId(userId));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourseById(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(courseService.getCourseById(courseId, userPrincipal.getUserId()));
    }

    @PostMapping
    public ResponseEntity<CourseDetailResponse> createCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateCourseRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(userPrincipal.getUserId(), request));
    }

}
