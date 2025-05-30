package com.thesoulapp.application.payment;

import com.thesoulapp.domain.payment.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentService {
    Transaction chargePatient(UUID patientId, UUID therapistId, BigDecimal amount, String paymentMethodId);
    List<Transaction> getTransactionsForTherapist(UUID therapistId);
    List<Transaction> getAllTransactions();
} 