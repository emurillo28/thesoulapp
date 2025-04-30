package com.thesoulapp.user.repository;

import com.thesoulapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByExternalId(String externalId);
    boolean existsByEmail(String email);
} 