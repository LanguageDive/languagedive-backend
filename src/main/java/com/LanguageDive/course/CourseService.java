package com.LanguageDive.course;

import com.LanguageDive.common.exception.ResourceNotFoundException;
import com.LanguageDive.course.dto.CourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseResponse> getAllCoursesByUserId(Long userId) {
        List<Course> courses = courseRepository.findAllByUserId(userId);
        return courses.stream().map(CourseResponse::toDto).toList();
    }
}
