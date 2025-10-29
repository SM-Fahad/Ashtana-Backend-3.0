package com.ashtana.backend.DTO.ResponseDTO;

import lombok.Data;

@Data
public class MyBagItemResponseDTO {
    private Long id;
    private String productName;
    private Double pricePerItem;
    private Integer quantity;
    private Double totalPrice;
}
