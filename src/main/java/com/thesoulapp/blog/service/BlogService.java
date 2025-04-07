package com.thesoulapp.blog.service;

import com.thesoulapp.blog.dto.BlogDto;
import com.thesoulapp.domain.model.Blog; // Assuming Blog model from domain
import com.thesoulapp.domain.model.BlogStatus;
import com.thesoulapp.blog.repository.BlogRepository; // Assuming BlogRepository exists
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public Page<BlogDto> getPublishedPosts(Pageable pageable) {
        return blogRepository.findByStatusOrderByPublishedAtDesc(BlogStatus.PUBLISHED, pageable)
                .map(this::mapBlogToDto);
    }

    @Transactional(readOnly = true)
    public BlogDto getPostBySlug(String slug) {
        return blogRepository.findBySlugAndStatus(slug, BlogStatus.PUBLISHED)
                .map(this::mapBlogToDto)
                .orElseThrow(() -> new EntityNotFoundException("Published blog post not found with slug: " + slug));
    }

    private BlogDto mapBlogToDto(Blog blog) {
        // Implement mapping logic
        return BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .content(blog.getContent())
                .authorName(blog.getAuthor() != null ? blog.getAuthor().getName() : "The Soul App") // Example
                .publishedAt(blog.getPublishedAt())
                // Add other fields like category, tags
                .build();
    }
    
     // Methods for creating/updating posts used by therapists
     // public BlogDto createPost(...) { ... }
}

// Define BlogDto
// Define BlogRepository 