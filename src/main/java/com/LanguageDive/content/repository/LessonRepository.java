package com.LanguageDive.content.repository;

import com.LanguageDive.content.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourseIdOrderByLessonOrderAsc(Long courseId);

    Optional<Lesson> findByIdAndCourseUserId(Long id, Long userId);
}
