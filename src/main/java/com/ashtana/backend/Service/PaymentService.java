package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.PaymentRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.PaymentResponseDTO;
import com.ashtana.backend.Entity.Order;
import com.ashtana.backend.Entity.Payment;
import com.ashtana.backend.Enums.PaymentStatus;
import com.ashtana.backend.Repository.OrderRepo;
import com.ashtana.backend.Repository.PaymentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;

    public PaymentService(PaymentRepo paymentRepo, OrderRepo orderRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
    }

    // ✅ DTO → Entity
    public Payment toEntity(PaymentRequestDTO dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + dto.getOrderId()));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }

    // ✅ Entity → DTO
    public PaymentResponseDTO toDto(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }

    // ✅ Create Payment
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Payment payment = toEntity(dto);
        Payment saved = paymentRepo.save(payment);
        return toDto(saved);
    }

    // ✅ Get Payment by ID
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
        return toDto(payment);
    }

    // ✅ Get All Payments
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PaymentResponseDTO updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));

        payment.setPaymentStatus(status);
        return toDto(paymentRepo.save(payment));
    }

    // ✅ Delete Payment
    public void deletePayment(Long id) {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
        paymentRepo.delete(payment);
    }
}
