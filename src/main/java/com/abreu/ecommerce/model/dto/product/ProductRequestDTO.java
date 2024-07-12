package com.abreu.ecommerce.model.dto.product;

public record ProductRequestDTO(
    String name,
    String description,
    Double price
) {
}
