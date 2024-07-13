package com.abreu.ecommerce.model.dto.order;

import com.abreu.ecommerce.model.Order;
import com.abreu.ecommerce.model.Product;

public record OrderResponseDTO (
        Long id,
        Integer quantity,
        Double price,
        Product product
) {

    public OrderResponseDTO(Order order) {
        this(order.getId(), order.getQuantity(), order.getPrice(), order.getProduct());
    }

}
