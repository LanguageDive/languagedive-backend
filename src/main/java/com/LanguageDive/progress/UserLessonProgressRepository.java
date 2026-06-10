package com.LanguageDive.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Long> {
}
