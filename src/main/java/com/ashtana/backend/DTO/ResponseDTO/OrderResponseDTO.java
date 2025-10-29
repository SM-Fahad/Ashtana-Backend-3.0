package com.ashtana.backend.DTO.ResponseDTO;


import com.ashtana.backend.Entity.Address;
import com.ashtana.backend.Enums.OrderStatus;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;
    private String userName;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private Address shippingAddress;

    // Use separate OrderItemResponseDTO class, no nested class
    private List<OrderItemResponseDTO> items;
}
