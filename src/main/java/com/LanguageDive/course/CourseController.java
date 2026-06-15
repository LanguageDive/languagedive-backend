package com.LanguageDive.course;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.course.dto.CourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    public ResponseEntity<List<CourseResponse>> getCoursesByUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        Long userId = userPrincipal.getUserId();
        return ResponseEntity.ok(courseService.getAllCoursesByUserId(userId));
    }


}
