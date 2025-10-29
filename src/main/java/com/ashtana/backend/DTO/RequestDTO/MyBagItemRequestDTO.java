package com.ashtana.backend.DTO.RequestDTO;

import lombok.Data;

@Data
public class MyBagItemRequestDTO {
    private Long MyBagId;
    private Long productId;
    private Integer quantity;
    private Long UserId;
}
