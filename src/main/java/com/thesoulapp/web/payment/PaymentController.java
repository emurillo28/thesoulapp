package com.thesoulapp.web.payment;

import com.thesoulapp.application.payment.PaymentService;
import com.thesoulapp.domain.payment.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Payments", description = "Endpoints for payment processing and transactions")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Charge a patient for an appointment (patients only)")
    @PostMapping("/payments")
    public ResponseEntity<?> charge(@RequestBody PaymentRequest request, Authentication authentication) {
        if (!hasRole(authentication, "PATIENT")) {
            return ResponseEntity.status(403).body("Only patients can make payments");
        }
        UUID patientId = UUID.fromString(getUserId(authentication));
        Transaction tx = paymentService.chargePatient(patientId, request.therapistId(), request.amount(), request.paymentMethodId());
        return ResponseEntity.ok(TransactionResponse.from(tx));
    }

    @Operation(summary = "Get transactions (therapists/admins only)")
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(Authentication authentication) {
        if (hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(paymentService.getAllTransactions().stream().map(TransactionResponse::from).toList());
        } else if (hasRole(authentication, "THERAPIST")) {
            UUID therapistId = UUID.fromString(getUserId(authentication));
            return ResponseEntity.ok(paymentService.getTransactionsForTherapist(therapistId).stream().map(TransactionResponse::from).toList());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private String getUserId(Authentication authentication) {
        return authentication.getDetails().toString();
    }

    @Builder
    public record PaymentRequest(UUID therapistId, BigDecimal amount, String paymentMethodId) {}
    @Builder
    public record TransactionResponse(String id, String patientId, String therapistId, BigDecimal amount, BigDecimal commission, String stripePaymentId, String status, String createdAt) {
        public static TransactionResponse from(Transaction t) {
            return TransactionResponse.builder()
                    .id(t.getId().toString())
                    .patientId(t.getPatientId().toString())
                    .therapistId(t.getTherapistId().toString())
                    .amount(t.getAmount())
                    .commission(t.getCommission())
                    .stripePaymentId(t.getStripePaymentId())
                    .status(t.getStatus())
                    .createdAt(t.getCreatedAt().toString())
                    .build();
        }
    }
} 