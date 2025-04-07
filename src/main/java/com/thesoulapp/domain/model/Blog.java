package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Blog {
    private UUID id;
    private String title;
    private String content;
    private String excerpt;
    private String slug;
    private BlogStatus status;
    private BlogCategory category;
    private List<String> tags;
    private UUID authorId;
    private String authorName;
    private String authorBio;
    private String authorAvatar;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime scheduledAt;
    private String featuredImage;
    private List<String> images;
    private List<String> videos;
    private List<String> attachments;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private boolean isFeatured;
    private boolean isPinned;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String canonicalUrl;
    private String language;
    private String translationOf;
    private List<String> translations;
    private String customData;
    
    // Domain methods
    public boolean isPublished() {
        return status == BlogStatus.PUBLISHED;
    }
    
    public boolean isDraft() {
        return status == BlogStatus.DRAFT;
    }
    
    public boolean isScheduled() {
        return status == BlogStatus.SCHEDULED;
    }
    
    public boolean isArchived() {
        return status == BlogStatus.ARCHIVED;
    }
    
    public void publish() {
        this.status = BlogStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }
    
    public void schedule(LocalDateTime scheduleTime) {
        this.status = BlogStatus.SCHEDULED;
        this.scheduledAt = scheduleTime;
    }
    
    public void archive() {
        this.status = BlogStatus.ARCHIVED;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    public void incrementCommentCount() {
        this.commentCount++;
    }
    
    public void incrementShareCount() {
        this.shareCount++;
    }
    
    public void addImage(String imageUrl) {
        if (!images.contains(imageUrl)) {
            images.add(imageUrl);
        }
    }
    
    public void addVideo(String videoUrl) {
        if (!videos.contains(videoUrl)) {
            videos.add(videoUrl);
        }
    }
    
    public void addAttachment(String attachmentUrl) {
        if (!attachments.contains(attachmentUrl)) {
            attachments.add(attachmentUrl);
        }
    }
    
    public void addTranslation(String translationId) {
        if (!translations.contains(translationId)) {
            translations.add(translationId);
        }
    }
    
    public boolean hasMedia() {
        return !images.isEmpty() || 
               !videos.isEmpty() || 
               !attachments.isEmpty();
    }
    
    public boolean isTranslated() {
        return !translations.isEmpty();
    }
    
    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
} 