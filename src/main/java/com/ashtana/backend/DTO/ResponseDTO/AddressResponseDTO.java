package com.ashtana.backend.DTO.ResponseDTO;

import lombok.Data;

@Data
public class AddressResponseDTO {

    private Long id;
    private String street;
    private String city;
    private String country;
    private String postalCode;
}
