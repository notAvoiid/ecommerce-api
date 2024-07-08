package com.abreu.ecommerce.model.dto;

public record ProductRequestDTO(
    String name,
    String description,
    Double price
) {
}
