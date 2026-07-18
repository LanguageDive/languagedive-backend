package com.LanguageDive.content.repository;

import com.LanguageDive.content.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByUserId(Long userId);

    Optional<Course> findByIdAndUserId(Long id, Long userId);
}
