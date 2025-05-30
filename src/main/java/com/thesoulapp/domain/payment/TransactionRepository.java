package com.thesoulapp.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTherapistId(UUID therapistId);
    List<Transaction> findByStatus(String status);
} 