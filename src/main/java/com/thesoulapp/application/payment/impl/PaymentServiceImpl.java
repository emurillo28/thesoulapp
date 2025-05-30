package com.thesoulapp.application.payment.impl;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.thesoulapp.application.payment.PaymentService;
import com.thesoulapp.domain.payment.Transaction;
import com.thesoulapp.domain.payment.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final TransactionRepository transactionRepository;
    private final BigDecimal COMMISSION_RATE = new BigDecimal("0.20"); // 20% commission

    @Autowired
    public PaymentServiceImpl(TransactionRepository transactionRepository, @Value("${stripe.api.key}") String stripeApiKey) {
        this.transactionRepository = transactionRepository;
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Transaction chargePatient(UUID patientId, UUID therapistId, BigDecimal amount, String paymentMethodId) {
        try {
            BigDecimal commission = amount.multiply(COMMISSION_RATE);
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount.multiply(new BigDecimal(100)).longValue()) // Stripe uses cents
                    .setCurrency("usd")
                    .setPaymentMethod(paymentMethodId)
                    .setConfirm(true)
                    .setDescription("Soul App Appointment Payment")
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            Transaction tx = Transaction.builder()
                    .patientId(patientId)
                    .therapistId(therapistId)
                    .amount(amount)
                    .commission(commission)
                    .stripePaymentId(intent.getId())
                    .status(intent.getStatus())
                    .build();
            return transactionRepository.save(tx);
        } catch (Exception e) {
            throw new RuntimeException("Payment failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaction> getTransactionsForTherapist(UUID therapistId) {
        return transactionRepository.findByTherapistId(therapistId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
} 