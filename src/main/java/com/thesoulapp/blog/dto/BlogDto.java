package com.thesoulapp.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlogDto {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String authorEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean published;
} 