package com.thesoulapp.blog.repository;

import com.thesoulapp.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthorEmailOrderByCreatedAtDesc(String authorEmail);
    List<Blog> findByPublishedTrueOrderByCreatedAtDesc();
} 