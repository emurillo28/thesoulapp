package com.thesoulapp.diary;

import com.thesoulapp.diary.controller.DiaryController;
import com.thesoulapp.diary.model.DiaryEntry;
import com.thesoulapp.diary.model.MoodType;
import com.thesoulapp.diary.repository.DiaryEntryRepository;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.model.UserRole;
import com.thesoulapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiaryControllerTest {

    @Autowired
    private DiaryController diaryController;

    @MockBean
    private DiaryEntryRepository diaryEntryRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private Authentication authentication;
    private DiaryEntry diaryEntry;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setRole(UserRole.PATIENT);

        authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

        diaryEntry = new DiaryEntry();
        diaryEntry.setId(1L);
        diaryEntry.setUser(user);
        diaryEntry.setContent("Test entry");
        diaryEntry.setMood(MoodType.HAPPY);
        diaryEntry.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void createEntry_ShouldSaveAndReturnEntry() {
        when(diaryEntryRepository.save(any(DiaryEntry.class))).thenReturn(diaryEntry);

        ResponseEntity<DiaryEntry> response = diaryController.createEntry(diaryEntry, authentication);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(diaryEntry, response.getBody());
        verify(diaryEntryRepository).save(any(DiaryEntry.class));
    }

    @Test
    void getEntries_ShouldReturnUserEntries() {
        List<DiaryEntry> entries = Arrays.asList(diaryEntry);
        Page<DiaryEntry> page = new PageImpl<>(entries);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(diaryEntryRepository.findByUserOrderByCreatedAtDesc(user, pageRequest))
                .thenReturn(page);

        ResponseEntity<Page<DiaryEntry>> response = diaryController.getEntries(pageRequest, authentication);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(page, response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void updateEntry_ShouldUpdateExistingEntry() {
        DiaryEntry updatedEntry = new DiaryEntry();
        updatedEntry.setContent("Updated content");
        updatedEntry.setMood(MoodType.SAD);

        when(diaryEntryRepository.findById(1L)).thenReturn(Optional.of(diaryEntry));
        when(diaryEntryRepository.save(any(DiaryEntry.class))).thenReturn(diaryEntry);

        ResponseEntity<DiaryEntry> response = diaryController.updateEntry(1L, updatedEntry, authentication);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(diaryEntryRepository).save(any(DiaryEntry.class));
    }

    @Test
    void deleteEntry_ShouldRemoveExistingEntry() {
        when(diaryEntryRepository.findById(1L)).thenReturn(Optional.of(diaryEntry));

        ResponseEntity<Void> response = diaryController.deleteEntry(1L, authentication);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(diaryEntryRepository).delete(diaryEntry);
    }
} 