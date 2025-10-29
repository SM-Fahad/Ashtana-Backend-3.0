package com.ashtana.backend.DTO.ResponseDTO;

import com.ashtana.backend.Enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {

        private Long id;
        private Long orderId;
        private BigDecimal amount;
        private String paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime paymentDate;
}
