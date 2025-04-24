package com.thesoulapp.diary.model;

import com.thesoulapp.domain.model.BaseEntity;
import com.thesoulapp.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary_entries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class DiaryEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Diary content cannot be blank")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Mood must be selected")
    private MoodType mood;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private boolean sharedWithTherapist = false;

    @Column
    private LocalDateTime sharedUntil;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }

    public boolean isSharedWithTherapist() {
        return sharedWithTherapist;
    }

    public boolean isCurrentlyShared() {
        return sharedWithTherapist && 
               (sharedUntil == null || sharedUntil.isAfter(LocalDateTime.now()));
    }

    public void shareWithTherapist(LocalDateTime until) {
        this.sharedWithTherapist = true;
        this.sharedUntil = until;
    }

    public void stopSharing() {
        this.sharedWithTherapist = false;
        this.sharedUntil = null;
    }

    public void updateContent(String newContent, MoodType newMood) {
        this.content = newContent;
        this.mood = newMood;
    }

    void setUser(User user) {
        this.user = user;
    }
} 