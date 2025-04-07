package com.thesoulapp.diary.repository;

import com.thesoulapp.diary.model.DiaryEntry;
import com.thesoulapp.diary.model.MoodType;
import com.thesoulapp.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
    Page<DiaryEntry> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    List<DiaryEntry> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
        User user, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT d.mood, COUNT(d) FROM DiaryEntry d WHERE d.user = ?1 AND d.createdAt BETWEEN ?2 AND ?3 GROUP BY d.mood")
    List<Object[]> getMoodStatistics(User user, LocalDateTime start, LocalDateTime end);
    
    List<DiaryEntry> findByUserAndMoodOrderByCreatedAtDesc(User user, MoodType mood);
} 