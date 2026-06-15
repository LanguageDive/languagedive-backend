package com.LanguageDive.course.dto;

import com.LanguageDive.course.Course;
import com.LanguageDive.course.SourceType;

public record CourseResponse(
    Long id,
    String title,
    String description,
    String coverUrl,
    SourceType sourceType
) {
    public static CourseResponse toDto(Course course){
        return new CourseResponse(course.getId(), course.getTitle(), course.getDescription(), course.getCoverUrl(), course.getSourceType());
    }
}
