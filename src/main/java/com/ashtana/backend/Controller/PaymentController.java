package com.ashtana.backend.Controller;


import com.ashtana.backend.DTO.RequestDTO.PaymentRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.PaymentResponseDTO;
import com.ashtana.backend.Enums.PaymentStatus;
import com.ashtana.backend.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ Create new payment
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    // ✅ Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // ✅ Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ✅ Update payment status
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
            ) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    // ✅ Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully.");
    }
}
