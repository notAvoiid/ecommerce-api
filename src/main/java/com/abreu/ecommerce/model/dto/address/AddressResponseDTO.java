package com.abreu.ecommerce.model.dto.address;

public record AddressResponseDTO(
    Long id,
    String zipCode,
    String number,
    String street,
    String neighborhood,
    String city,
    String state
) {
}
