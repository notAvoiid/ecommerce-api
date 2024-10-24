package com.abreu.ecommerce.model.dto.cart;

public record CartUpdateDTO(
    Long id,
    Integer quantity,
    Long productId
) {
}
