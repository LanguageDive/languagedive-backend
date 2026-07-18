package com.LanguageDive.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Long> {
}
