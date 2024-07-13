package com.abreu.ecommerce.model.dto.order;

public record OrderRequestDTO (
    Integer quantity,
    Long productId
) {
}
