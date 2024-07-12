package com.abreu.ecommerce.model.dto.product;

public record ProductUpdateDTO(
    Long id,
    String name,
    String description,
    Double price
) {
}
