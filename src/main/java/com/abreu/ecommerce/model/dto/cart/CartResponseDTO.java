package com.abreu.ecommerce.model.dto.cart;

import com.abreu.ecommerce.model.Cart;
import com.abreu.ecommerce.model.Product;

public record CartResponseDTO(
        Long id,
        Integer quantity,
        Double price,
        Product product
) {

    public CartResponseDTO(Cart cart) {
        this(cart.getId(), cart.getQuantity(), cart.getPrice(), cart.getProduct());
    }

}
