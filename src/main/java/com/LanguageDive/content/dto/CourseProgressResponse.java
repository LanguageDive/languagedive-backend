package com.LanguageDive.content.dto;

import com.LanguageDive.progress.entity.UserCourseProgress;

public record CourseProgressResponse(
        Integer completedLessons,
        Integer totalLessons,
        Integer progressPercentage
) {
    public static CourseProgressResponse from(UserCourseProgress progress) {
        return new CourseProgressResponse(
                progress.getCompletedLessons(),
                progress.getTotalLessons(),
                progress.getProgressPercentage()
        );
    }
}
