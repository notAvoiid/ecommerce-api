package com.abreu.ecommerce.model.dto.cart;

public record CartRequestDTO(
    Integer quantity,
    Long productId
) {
}
