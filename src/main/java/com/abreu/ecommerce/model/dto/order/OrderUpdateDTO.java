package com.abreu.ecommerce.model.dto.order;

public record OrderUpdateDTO(
    Long id,
    Integer quantity,
    Long productId
) {
}
