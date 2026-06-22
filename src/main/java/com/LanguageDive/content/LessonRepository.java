package com.LanguageDive.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourseIdOrderByLessonOrderAsc(Long courseId);

    Optional<Lesson> findByIdAndCourseUserId(Long id, Long userId);
}
