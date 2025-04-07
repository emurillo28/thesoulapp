package com.thesoulapp.diary.controller;

import com.thesoulapp.diary.model.DiaryEntry;
import com.thesoulapp.diary.model.MoodType;
import com.thesoulapp.diary.repository.DiaryEntryRepository;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<DiaryEntry> createEntry(@RequestBody DiaryEntry entry, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        entry.setUser(user);
        return ResponseEntity.ok(diaryEntryRepository.save(entry));
    }

    @GetMapping
    public ResponseEntity<Page<DiaryEntry>> getEntries(Pageable pageable, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(diaryEntryRepository.findByUserOrderByCreatedAtDesc(user, pageable));
    }

    @GetMapping("/mood-stats")
    public ResponseEntity<Map<MoodType, Long>> getMoodStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Authentication authentication) {
        
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<MoodType, Long> stats = diaryEntryRepository.getMoodStatistics(user, start, end)
                .stream()
                .collect(Collectors.toMap(
                    row -> (MoodType) row[0],
                    row -> (Long) row[1]
                ));
        
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryEntry> updateEntry(
            @PathVariable Long id,
            @RequestBody DiaryEntry updatedEntry,
            Authentication authentication) {
        
        return diaryEntryRepository.findById(id)
                .map(entry -> {
                    User user = userRepository.findByEmail(authentication.getName())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    
                    if (!entry.getUser().getId().equals(user.getId())) {
                        throw new RuntimeException("Not authorized to update this entry");
                    }
                    
                    entry.setContent(updatedEntry.getContent());
                    entry.setMood(updatedEntry.getMood());
                    return ResponseEntity.ok(diaryEntryRepository.save(entry));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id, Authentication authentication) {
        return diaryEntryRepository.findById(id)
                .map(entry -> {
                    User user = userRepository.findByEmail(authentication.getName())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    
                    if (!entry.getUser().getId().equals(user.getId())) {
                        throw new RuntimeException("Not authorized to delete this entry");
                    }
                    
                    diaryEntryRepository.delete(entry);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 