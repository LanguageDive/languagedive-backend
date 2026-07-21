package com.LanguageDive.content.repository;

import com.LanguageDive.content.entity.LessonSentence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonSentenceRepository extends JpaRepository<LessonSentence, Long> {

    List<LessonSentence> findByLessonIdOrderBySentenceIndexAsc(Long lessonId);

    long countByLessonId(Long lessonId);
}
