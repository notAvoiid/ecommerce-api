package com.abreu.ecommerce.model.dto;

public record ProductUpdateDTO(
    Long id,
    String name,
    String description,
    Double price
) {
}
