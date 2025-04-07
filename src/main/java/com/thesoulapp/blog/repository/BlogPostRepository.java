package com.thesoulapp.blog.repository;

import com.thesoulapp.blog.model.BlogPost;
import com.thesoulapp.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Page<BlogPost> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);
    Page<BlogPost> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    long countByAuthor(User author);
    long countByPublishedTrue();
} 